package com.example.babyapiscanner.scanner.libScannerImpl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import com.example.babyapiscanner.scanner.LibScanner;
import com.example.babyapiscanner.scanner.bo.LibInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PomXmlLibScanner implements LibScanner {

    @Override
    public List<LibInfo> findLibListInFile(String filePath) {
        Document pomXml = XmlUtil.readXML(filePath);
        Element project = XmlUtil.getRootElement(pomXml);
        LibInfo parentLibInfo = findParentLibInfo(project);
        List<LibInfo> dependencyLibInfo = findDependencyLibInfo(project, parentLibInfo.getVersion());

        List<LibInfo> libList = new ArrayList<>();
        libList.add(parentLibInfo);
        libList.addAll(dependencyLibInfo);
        return libList;
    }

    public static LibInfo findParentLibInfo(Element project) {
        Element parent = XmlUtil.getElement(project, "parent");
        String groupId = XmlUtil.getElement(parent, "groupId").getTextContent();
        String artifactId = XmlUtil.getElement(parent, "artifactId").getTextContent();
        String version = XmlUtil.getElement(parent, "version").getTextContent();
        return new LibInfo(groupId, artifactId, version);
    }

    public static List<LibInfo> findDependencyLibInfo(Element project, String parentVersion) {
        Element dependencies = XmlUtil.getElement(project, "dependencies");
        List<Element> dependencyList = XmlUtil.getElements(dependencies, "dependency");

        return dependencyList.stream()
                .map(dependency -> {
                    String groupId = XmlUtil.getElement(dependency, "groupId").getTextContent();
                    String artifactId = XmlUtil.getElement(dependency, "artifactId").getTextContent();

                    Element versionElement = XmlUtil.getElement(dependency, "version");
                    String version = ObjectUtil.isNotNull(versionElement) ? versionElement.getTextContent() : parentVersion;
                    return new LibInfo(groupId, artifactId, version);
                }).collect(Collectors.toList());
    }
}
