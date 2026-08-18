package pe.factos.billing.domain.port;

public interface StorageGateway {
    void storeXml(String series, String correlative, byte[] xmlContent);
    void storeCdr(String series, String correlative, byte[] cdrZipContent);
}
