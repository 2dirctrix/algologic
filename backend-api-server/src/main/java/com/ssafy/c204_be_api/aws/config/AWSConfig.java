package com.ssafy.c204_be_api.aws.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class AWSConfig {

    private final AWSProperties awsProperties;
    private static final Integer API_CALL_TIMEOUT_SECONDS = 30;
    private static final Integer API_CALL_ATTEMPT_TIMEOUT_SECONDS = 1000;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .overrideConfiguration(
                b -> b.apiCallTimeout(Duration.ofSeconds(API_CALL_TIMEOUT_SECONDS))
                    .apiCallAttemptTimeout(Duration.ofMillis(API_CALL_ATTEMPT_TIMEOUT_SECONDS))
            )
            .credentialsProvider(awsProperties::getCredentials)
            .region(Region.of(awsProperties.getRegion()))
            .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
            .region(Region.of(awsProperties.getRegion()))
            .credentialsProvider(awsProperties::getCredentials)
            .build();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .credentialsProvider(awsProperties::getCredentials)
                .region(Region.of(awsProperties.getRegion()))
                .build();
    }
}
