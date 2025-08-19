package com.ssafy.c204_be_api.config.property;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.List;

@Getter
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

    private final List<String> allowedOrigins;
    private final Boolean allowCredentials;
    private final List<String> allowedHeaders;
    private final List<String> exposedHeaders;
    private final List<String> allowedMethods;
    private final String pathPattern;

    @ConstructorBinding
    public CorsProperties(List<String> allowedOrigins, Boolean allowCredentials, List<String> allowedHeaders, List<String> exposedHeaders, List<String> allowedMethods, String pathPattern) {
        this.allowedOrigins = allowedOrigins;
        this.allowCredentials = allowCredentials;
        this.allowedHeaders = allowedHeaders;
        this.exposedHeaders = exposedHeaders;
        this.allowedMethods = allowedMethods;
        this.pathPattern = pathPattern;
    }

}
