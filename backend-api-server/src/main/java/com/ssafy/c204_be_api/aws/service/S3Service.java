package com.ssafy.c204_be_api.aws.service;

import com.ssafy.c204_be_api.aws.config.AWSProperties;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class S3Service {

    private final S3Presigner s3Presigner;
    private final AWSProperties awsProperties;
    private final S3Client s3Client;

    private static final String ENCODING = "utf-8";
    private static final Integer PRESIGNED_URL_EXPIRATION_MINUTES = 5;

    /**
     * key에 해당하는 파일의 presigned URL을 생성합니다.
     *
     * @param key S3 버킷 내의 파일 키
     * @throws IOException
     */
    public String getPresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(awsProperties.getS3BucketName())
                .key(key)
                .responseContentEncoding(ENCODING)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(PRESIGNED_URL_EXPIRATION_MINUTES))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    public String uploadImage(MultipartFile image) {
        validateImage(image);
        try {
            String s3FileName = "profileImages/" + UUID.randomUUID() + image.getOriginalFilename();

            byte[] bytes = image.getBytes();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getS3BucketName())
                    .key(s3FileName)
                    .contentType(image.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(bytes)
            );

            return s3FileName;
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 업로드 중 문제가 발생하였습니다.", e);
        }
    }

    public void deleteImage(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(awsProperties.getS3BucketName())
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            log.error("S3 삭제 실패: key={}, message={}", key, e.awsErrorDetails().errorMessage());
        }
    }

    private void validateImage(MultipartFile image) {
        if (image.isEmpty() || Objects.isNull(image.getOriginalFilename())) {
            throw new IllegalArgumentException("업로드할 파일이 비어 있거나 파일명이 없습니다.");
        }

        validateImageExtension(image.getOriginalFilename());
    }

    private void validateImageExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("파일의 확장자가 존재하지 않습니다.");
        }

        List<String> allowedExtentionList = List.of("jpg", "jpeg", "png");

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        allowedExtentionList.stream().filter(allowedExtension -> allowedExtension.equals(extension))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[확장자 : %s]는 지원되지 않는 확장자입니다.".formatted(extension)));
    }
}
