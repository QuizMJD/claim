package com.t3h.insuranceclaim.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class MinioService {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    private MinioClient minioClient;

    public MinioService() {
        this.minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String uploadBase64Image(String base64Image, String folder) {
        try {
            // Remove data:image/jpeg;base64, prefix if exists
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            // Decode base64 to byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + ".jpg";
            String objectName = folder + "/" + filename;

            // Upload to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                            .contentType("image/jpeg")
                            .build()
            );

            // Generate URL
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .method(Method.GET)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image to MinIO", e);
        }
    }
} 