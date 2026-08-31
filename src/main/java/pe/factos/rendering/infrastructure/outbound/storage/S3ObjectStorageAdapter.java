package pe.factos.rendering.infrastructure.outbound.storage;

import org.springframework.stereotype.Component;
import pe.factos.rendering.domain.port.ObjectStoragePort;
import pe.factos.shared.domain.BusinessException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class S3ObjectStorageAdapter implements ObjectStoragePort {
    private final S3Client s3Client;

    public S3ObjectStorageAdapter(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(String bucketName, String objectKey, byte[] content, String contentType) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(contentType)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(content));
            return objectKey;
        } catch (Exception e) {
            throw new BusinessException("Failed to upload object to Cloudflare R2: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadFile(String bucketName, String objectKey) {
        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            return s3Client.getObjectAsBytes(request).asByteArray();
        } catch (Exception e) {
            throw new BusinessException("Failed to download object from Cloudflare R2: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String bucketName, String objectKey) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            s3Client.deleteObject(request);
        } catch (Exception e) {
            throw new BusinessException("Failed to delete object from Cloudflare R2: " + e.getMessage());
        }
    }
}
