package reward.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.net.URL;

@Service
public class S3Service {

    private final S3Client s3Client;
    private static final String BUCKETNAME = "cmux-reward";

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = generateFileName(file.getOriginalFilename());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKETNAME)
                .key(fileName)
                .build();
        PutObjectResponse response = s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        while (true) {
            if (response != null) {
                break;
            }
        }
        return getFileUrl(fileName);
    }

    private String generateFileName(String originalFilename) {
        return Instant.now().getEpochSecond() + "_" + originalFilename;
    }

    private String getFileUrl(String fileName) {
        URL url = s3Client.utilities().getUrl(builder -> builder.bucket(BUCKETNAME).key(fileName));
        return url.toString();
    }
}
