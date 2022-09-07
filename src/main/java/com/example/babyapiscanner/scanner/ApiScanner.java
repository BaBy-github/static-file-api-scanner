package com.example.babyapiscanner.scanner;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.babyapiscanner.entity.Lib;
import com.example.babyapiscanner.entity.Project;
import com.example.babyapiscanner.entity.ProjectApi;
import com.example.babyapiscanner.repository.LibRepo;
import com.example.babyapiscanner.repository.ProjectApiRepo;
import com.example.babyapiscanner.repository.ProjectRepo;
import com.example.babyapiscanner.scanner.bo.ApiInfo;
import com.example.babyapiscanner.scanner.bo.LibInfo;
import com.example.babyapiscanner.scanner.fileApiScannerImpl.SpringMVCAnnotationFileApiScanner;
import com.example.babyapiscanner.scanner.libScannerImpl.PomXmlLibScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

@Component
public class ApiScanner {
    @Autowired
    private ProjectRepo projectRepo;
    @Autowired
    private ProjectApiRepo projectApiRepo;
    @Autowired
    private LibRepo libRepo;

    public void scanProject() {
        // 1.拉gitlab仓库到本地 返回绝对路径
        String projectLocalPath = "";
        Project project = new Project();
        project.setId(UUID.randomUUID().toString());
        project.setGitUrl(projectLocalPath);
        project.setCreateTime(new Timestamp(System.currentTimeMillis()));
        Project savedProject = projectRepo.save(project);

        // 2.扫描FileApiScanner扫描文件的Api
        FileApiScanner fileApiScanner = new SpringMVCAnnotationFileApiScanner();
        List<ApiInfo> fileApiList = fileApiScanner.findApiListInFolder(projectLocalPath);
        // 2.1 fileApiList保存到DB
        fileApiList.forEach(apiInfo -> {
            ProjectApi projectApi = new ProjectApi();
            projectApi.setId(UUID.randomUUID().toString());
            projectApi.setProject(savedProject);
            projectApi.setApi(apiInfo.getApi());
            projectApi.setMethod(apiInfo.getMethod());
            projectApi.setFilePath(apiInfo.getFilePath().substring(projectLocalPath.length()));

            ProjectApi savedProjectApi = projectApiRepo.save(projectApi);
        });

        // 3.扫描项目中正在使用的lib
        LibScanner libScanner;
        List<LibInfo> projectUsingLibList = new ArrayList<>();

        String pomXmlPath = findPomXmlPath(projectLocalPath);
        if (pomXmlPath != null) {
            libScanner = new PomXmlLibScanner();
            projectUsingLibList = libScanner.findLibListInFile(pomXmlPath);
        }

        // 3.1保存project-lib到DB （1）有lib不管 （2）无lib插入数据 is_management=false
        List<Lib> projectLibList = new ArrayList<>();
        projectUsingLibList.forEach(libInfo -> {
            Lib lib = libRepo.findByGroupIdAndArtifactIdAndVersion(libInfo.getGroupId(), libInfo.getArtifactId(), libInfo.getVersion());
            if (lib == null) {
                Lib libToDB = new Lib();
                libToDB.setId(UUID.randomUUID().toString());
                libToDB.setGroupId(libInfo.getGroupId());
                libToDB.setArtifactId(libInfo.getArtifactId());
                libToDB.setVersion(libInfo.getVersion());
                libToDB.setIsManagement(false);
                lib = libRepo.save(libToDB);
            }
            projectLibList.add(lib);
        });
        savedProject.setLibs(projectLibList);
        projectRepo.save(savedProject);
        System.out.println("<< done");
    }

    public static String findPomXmlPath(String projectLocalPath) {
        return findFilePathByFileNameInFolder(projectLocalPath, "pom.xml");
    }

    public static String findFilePathByFileNameInFolder(String projectLocalPath, String fileName) {
        String filePath = null;
        for (File file : FileUtil.ls(projectLocalPath)) {
            if (file.isDirectory()) {
                filePath = findFilePathByFileNameInFolder(file.getAbsolutePath(), fileName);
                if (ObjectUtil.isNotNull(filePath)) {
                    return filePath;
                }
            } else if (fileName.equals(file.getName())) {
                return file.getAbsolutePath();
            }
        }
        return filePath;
    }
}
