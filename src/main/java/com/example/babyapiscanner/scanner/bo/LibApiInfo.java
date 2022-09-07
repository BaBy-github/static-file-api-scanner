package com.example.babyapiscanner.scanner.bo;

import lombok.Data;

@Data
public class LibApiInfo {
    private String method;
    private String api;
    private String filePath;
    private LibInfo libInfo;
}
