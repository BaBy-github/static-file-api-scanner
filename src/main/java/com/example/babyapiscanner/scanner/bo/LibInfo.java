package com.example.babyapiscanner.scanner.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LibInfo {
    private String groupId;
    private String artifactId;
    private String version;
}
