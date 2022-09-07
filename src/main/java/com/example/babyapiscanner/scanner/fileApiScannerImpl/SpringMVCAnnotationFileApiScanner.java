package com.example.babyapiscanner.scanner.fileApiScannerImpl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.example.babyapiscanner.scanner.FileApiScanner;
import com.example.babyapiscanner.scanner.bo.ApiInfo;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

public class SpringMVCAnnotationFileApiScanner implements FileApiScanner {
    @Override
    public List<ApiInfo> findApiListInFolder(String folderPath) {
        List<ApiInfo> apiInfoList = new ArrayList<>();
        for (File file : FileUtil.ls(folderPath)) {
            if (file.isDirectory()) {
                List<ApiInfo> apiInFolder = findApiListInFolder(file.getAbsolutePath());
                if (ObjectUtil.isNotNull(apiInFolder)) {
                    apiInfoList.addAll(apiInFolder);
                }
            } else {
                List<ApiInfo> apiInFile = findApiListInFile(file.getAbsolutePath());
                if (ObjectUtil.isNotNull(apiInFile)) {
                    apiInfoList.addAll(apiInFile);
                }
            }
        }
        return apiInfoList;
    }

    @Override
    public List<ApiInfo> findApiListInFile(String filePath) {
        String fileType = FileUtil.getType(new File(filePath));
        List<ApiInfo> apiList = new ArrayList<>();
        if ("java".equals(fileType)) {
            List<ApiInfo> apiInJavaFile = findApiInJavaFile(filePath);
            if (ObjectUtil.isNotNull(apiInJavaFile)) {
                apiList.addAll(apiInJavaFile);
            }
        }
        return apiList;
    }

    /**
     * 仅支持springBoot的Mapping注解扫描
     */
    public static List<ApiInfo> findApiInJavaFile(String filePath) {
        FileReader fileReader = new FileReader(filePath);
        String fileString = fileReader.readString();
        if (!fileString.contains("public class")) return null;
        HashMap<String, Integer> indexMap = new HashMap<>();
        findClassContent(indexMap, fileString);
        List<String> mappingList = findAllMapping(StrUtil.sub(fileString, indexMap.get("indexOf__class_{_start"), indexMap.get("indexOf__class_end_}")));

        String classMVCMappingPath = findMappingPath(findClassAnnotationMapping(StrUtil.sub(fileString, 0, indexMap.get("indexOf__class_{_start"))));
        List<ApiInfo> apiList = new ArrayList<>();
        for (String mapping : mappingList) {
            String api = classMVCMappingPath + findMappingPath(mapping);
            String method = findHttpMethodInAnnotationMapping(mapping);
            apiList.add(new ApiInfo(method, api, filePath));
        }
        return apiList;
    }

    public static String findHttpMethodInAnnotationMapping(String annotationMapping) {
        int indexOf__start = annotationMapping.indexOf("@");
        int indexOf__end = annotationMapping.indexOf("Mapping");
        String method = StrUtil.sub(annotationMapping, indexOf__start + 1, indexOf__end);
        return "Request".equals(method) ? "All" : method;
    }

    public static String findClassAnnotationMapping(String classBeforeText) {
        List<String> allMapping = findAllMapping(classBeforeText);
        if (allMapping.size() == 0) {
            return "";
        }
        return allMapping.get(0);
    }

    public static HashMap<String, Integer> findClassContent(HashMap<String, Integer> indexMap, String fileString) {
        int indexOf__public_class = fileString.indexOf("public class ");
        Stack<Character> stringStack = new Stack<>();

        int indexOf__class_start = 0;
        int indexOf__class_end = 0;
        for (int index = indexOf__public_class; index < fileString.length(); index++) {
            char charAtI = fileString.charAt(index);
            if (charAtI == '(') stringStack.push(charAtI);
            else if (charAtI == ')') stringStack.pop();
            else if (charAtI == '{') {
                stringStack.push(charAtI);
                if (stringStack.size() == 1) {
                    indexOf__class_start = index;
                }
            } else if (charAtI == '}') {
                stringStack.pop();
                if (stringStack.size() == 0) {
                    indexOf__class_end = index;
                    break;
                }
            }
        }
        indexMap.put("indexOf__class_{_start", indexOf__class_start + 1);
        indexMap.put("indexOf__class_end_}", indexOf__class_end);
        return indexMap;
    }

    public static List<String> findAllMapping(String classContent) {
//        String str = "@RequestMapping(value = \"/get/{id}\")";
        String s = "@(Get|Post|Put|Request|Delete|Patch|Message|Connect|Subscribe|Discriminator)Mapping\\([ ,{}\\(/\\\"\\w\\)=]*\\)";
        return ReUtil.findAll(s, classContent, 0, new ArrayList<>());
    }

    public static String findMappingPath(String mappingContent) {
        if (!mappingContent.contains(",")) { // 不包含参数
            return findPathInDoubleQuotationMarks(mappingContent);
        } else {
            String s = "value\\s*=\\s*[\"\\w\\{}/]*[,)]";
            String valueItem = ReUtil.get(s, mappingContent, 0);
            return findPathInDoubleQuotationMarks(valueItem);
        }
    }

    public static String findPathInDoubleQuotationMarks(String str) {
        int indexOf__path_start = str.indexOf("\"");
        int indexOf__path_end = str.lastIndexOf("\"");
        return StrUtil.sub(str, indexOf__path_start + 1, indexOf__path_end);
    }

    /**
     * bug
     */
    public static String cleanCommons(String content) {
        content = content.replaceAll("//.+\\r\\n", "");
        return Pattern.compile("/\\*.+?\\*/", Pattern.DOTALL).matcher(content).replaceAll("");
    }
}
