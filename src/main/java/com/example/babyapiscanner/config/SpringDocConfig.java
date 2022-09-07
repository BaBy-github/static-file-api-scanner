package com.example.babyapiscanner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: BaBy
 * @Date: 2022/8/20 16:53
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "baby-api-scanner",
                description = "api-scanner",
                version = "1.0"
        ),
        security = @SecurityRequirement(name = "Authorization")
)
//@SecurityScheme(
//        name = "Authorization",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
public class SpringDocConfig {
}
