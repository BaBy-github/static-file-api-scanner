package com.example.babyapiscanner.controller;

import com.example.babyapiscanner.scanner.ApiScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: BaBy
 * @Date: 2022/9/6 18:40
 */
@RestController
public class ApiScannerController {
    @Autowired
    private ApiScanner apiScanner;

    @GetMapping("/trigger")
    public void trigger() {
        apiScanner.scanProject();
    }
}
