package com.example.babyapiscanner.scanner.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LibApiAnalyzerResult {
    private List<LibApiInfo> ApiInfoList;
    private List<LibInfo> unMatchLibList;
}
