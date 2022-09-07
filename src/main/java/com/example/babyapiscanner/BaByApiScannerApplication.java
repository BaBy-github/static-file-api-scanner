package com.example.babyapiscanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BaByApiScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaByApiScannerApplication.class, args);
    }

}
