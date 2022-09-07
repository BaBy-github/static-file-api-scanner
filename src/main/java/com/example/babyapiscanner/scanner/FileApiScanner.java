package com.example.babyapiscanner.scanner;


import com.example.babyapiscanner.scanner.bo.ApiInfo;

import java.util.List;

public interface FileApiScanner {
    List<ApiInfo> findApiListInFolder(String folderPath);

    List<ApiInfo> findApiListInFile(String filePath);
}
