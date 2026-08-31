package pe.factos.rendering.infrastructure.outbound.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class StorageConfiguration {

    @Value("${application.storage.s3.endpoint:https://8d009c5a27b703c2906fd1b00b96908c.r2.cloudflarestorage.com}")
    private String endpointUrl;

    @Value("${application.storage.s3.access-key:f84173333e47e104c44c7b0881e1cbc5}")
    private String accessKey;

    @Value("${application.storage.s3.secret-key:3ca7f93131e22ccfb157b65af299a16b8f169e93a86e70b02e9d2db3229709d6}")
    private String secretKey;

    @Value("${application.storage.s3.region:auto}")
    private String region;

    @Bean
    public S3Client s3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .endpointOverride(URI.create(endpointUrl))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region.equalsIgnoreCase("auto") ? "us-east-1" : region))
                .forcePathStyle(true)
                .build();
    }
}
