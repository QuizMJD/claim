package com.t3h.insuranceclaim.service;

import com.t3h.insuranceclaim.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadBase64Image(String base64Image, String folder) {
        try {
            if (StringUtils.isEmpty(base64Image)) {
                return null;
            }

            // Remove data:image/jpeg;base64, prefix if exists
            if (base64Image.contains(",")) {
                base64Image = base64Image.split(",")[1];
            }

            // Decode base64 to byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            
            // Validate image size
            if (imageBytes.length > 5 * 1024 * 1024) { // 5MB limit
                throw new IllegalArgumentException("Image size exceeds 5MB limit");
            }

            // Generate unique filename
            String filename = UUID.randomUUID().toString() + ".jpg";
            String objectName = folder + "/" + filename;

            // Upload to MinIO
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .stream(new ByteArrayInputStream(imageBytes), imageBytes.length, -1)
                            .contentType("image/jpeg")
                            .build()
            );

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(minioConfig.getBucket())
                            .object(objectName)
                            .method(Method.GET)
                            .build()
            );

        } catch (Exception e) {
            log.error("Error uploading image to MinIO: {}", e.getMessage());
            throw new RuntimeException("Failed to upload image to MinIO", e);
        }
    }
} 