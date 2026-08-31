package pe.factos.rendering.domain.port;

/**
 * Domain port for Object Storage operations (e.g. Cloudflare R2 / AWS S3).
 */
public interface ObjectStoragePort {
    String uploadFile(String bucketName, String objectKey, byte[] content, String contentType);
    byte[] downloadFile(String bucketName, String objectKey);
    void deleteFile(String bucketName, String objectKey);
}
