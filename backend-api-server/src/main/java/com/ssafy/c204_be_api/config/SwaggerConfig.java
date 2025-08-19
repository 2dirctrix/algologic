package com.ssafy.c204_be_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME = "Bearer";
    private static final String BEARER_FORMAT = "JWT";

    @Bean
    public OpenAPI api() {
        SecurityScheme apiKey = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION)
                .scheme(SECURITY_SCHEME)
                .bearerFormat(BEARER_FORMAT);

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("TEAM C204 REST API Specifications")
                .description("C204 SSAFY 공통 프로젝트 REST API 문서")
                .version("1.0.0");
    }
}
