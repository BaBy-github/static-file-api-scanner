package com.example.babyapiscanner.scanner;


import com.example.babyapiscanner.scanner.bo.LibInfo;

import java.util.List;

public interface LibScanner {
    List<LibInfo> findLibListInFile(String filePath);
}
