package pe.factos.billing.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.factos.billing.domain.port.StorageGateway;
import pe.factos.shared.domain.BusinessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileSystemStorageGatewayAdapter implements StorageGateway {

    private final Path baseStoragePath;

    public FileSystemStorageGatewayAdapter(@Value("${factos.storage.base-path:storage/cpe}") String basePath) {
        this.baseStoragePath = Paths.get(basePath);
        initDirectories();
    }

    private void initDirectories() {
        try {
            Files.createDirectories(baseStoragePath.resolve("xml"));
            Files.createDirectories(baseStoragePath.resolve("cdr"));
        } catch (IOException e) {
            throw new BusinessException("Failed to initialize CPE file storage directories: " + e.getMessage());
        }
    }

    @Override
    public void storeXml(String series, String correlative, byte[] xmlContent) {
        String filename = String.format("%s-%s.xml", series, correlative);
        Path targetPath = baseStoragePath.resolve("xml").resolve(filename);
        try {
            Files.write(targetPath, xmlContent);
        } catch (IOException e) {
            throw new BusinessException("Failed to store CPE XML file: " + e.getMessage());
        }
    }

    @Override
    public void storeCdr(String series, String correlative, byte[] cdrZipContent) {
        String filename = String.format("R-%s-%s.zip", series, correlative);
        Path targetPath = baseStoragePath.resolve("cdr").resolve(filename);
        try {
            Files.write(targetPath, cdrZipContent);
        } catch (IOException e) {
            throw new BusinessException("Failed to store SUNAT CDR file: " + e.getMessage());
        }
    }
}
