package pe.factos.billing.infrastructure.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;
import pe.factos.TestPersistenceApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = TestPersistenceApplication.class,
        properties = "factos.storage.base-path=target/test-storage"
)
class FileSystemStorageGatewayAdapterTest {

    @Autowired
    private FileSystemStorageGatewayAdapter storageGateway;

    private final Path testStoragePath = Paths.get("target/test-storage");

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(testStoragePath);
    }

    @Test
    void shouldStoreXmlAndCdrFiles() throws IOException {
        // Arrange
        String series = "F001";
        String correlative = "00000001";
        byte[] xmlContent = "<?xml version=\"1.0\"?><Invoice/>".getBytes(StandardCharsets.UTF_8);
        byte[] cdrContent = "CDR-ZIP-DATA-MOCK".getBytes(StandardCharsets.UTF_8);

        // Act
        storageGateway.storeXml(series, correlative, xmlContent);
        storageGateway.storeCdr(series, correlative, cdrContent);

        // Assert
        Path expectedXmlPath = testStoragePath.resolve("xml").resolve("F001-00000001.xml");
        Path expectedCdrPath = testStoragePath.resolve("cdr").resolve("R-F001-00000001.zip");

        assertThat(Files.exists(expectedXmlPath)).isTrue();
        assertThat(Files.exists(expectedCdrPath)).isTrue();
        assertThat(Files.readAllBytes(expectedXmlPath)).isEqualTo(xmlContent);
        assertThat(Files.readAllBytes(expectedCdrPath)).isEqualTo(cdrContent);
    }
}
