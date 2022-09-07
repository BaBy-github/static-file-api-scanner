package com.example.babyapiscanner.scanner.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiInfo {
    private String method;
    private String api;
    private String filePath;
}
