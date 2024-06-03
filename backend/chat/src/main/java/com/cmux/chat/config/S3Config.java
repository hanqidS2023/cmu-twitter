package com.cmux.chat.config;

import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class S3Config {
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                       .region(Region.of("us-east-2"))
                       .build();
    }
}
