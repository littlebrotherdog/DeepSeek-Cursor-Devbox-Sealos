package com.example.hello.util;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.public-url-prefix}")
    private String publicUrlPrefix;

    public String upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName = UUID.randomUUID() + ext;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
        return publicUrlPrefix + objectName;
    }
} 