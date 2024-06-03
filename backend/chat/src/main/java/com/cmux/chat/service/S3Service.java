package com.cmux.chat.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName = "cmux-chat";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                                                            .bucket(bucketName)
                                                            .key(fileName)
                                                            .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        return getFileUrl(fileName);
    }

    private String generateFileName(String originalFilename) {
        return Instant.now().getEpochSecond() + "_" + originalFilename;
    }

    private String getFileUrl(String fileName) {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(fileName));
        return url.toString();
    }
}
