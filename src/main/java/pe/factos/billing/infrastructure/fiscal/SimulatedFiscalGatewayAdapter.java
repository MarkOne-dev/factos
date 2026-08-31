package pe.factos.billing.infrastructure.fiscal;

import org.springframework.stereotype.Component;
import pe.factos.billing.domain.model.Cpe;
import pe.factos.billing.domain.port.CpeSubmissionResult;
import pe.factos.billing.domain.port.FiscalGateway;
import pe.factos.shared.domain.BusinessException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class SimulatedFiscalGatewayAdapter implements FiscalGateway {

    @Override
    public CpeSubmissionResult submit(Cpe cpe) {
        byte[] signedXml = createMockSignedXml(cpe);

        if (cpe.getCorrelative().endsWith("99")) {
            String errorCode = "1034";
            String errorMessage = String.format("El comprobante %s-%s ya existe en los registros de SUNAT.",
                    cpe.getSeries(), cpe.getCorrelative());
            return CpeSubmissionResult.failure(errorCode, errorMessage, signedXml);
        }

        String cdrDescription = String.format("El comprobante %s-%s ha sido aceptado",
                cpe.getSeries(), cpe.getCorrelative());
        byte[] cdrZip = createMockCdrZip(cpe.getSeries(), cpe.getCorrelative());

        return CpeSubmissionResult.success(cdrDescription, signedXml, cdrZip);
    }

    private byte[] createMockSignedXml(Cpe cpe) {
        String xml = String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<Invoice xmlns=\"urn:oasis:names:specification:ubl:schema:xsd:Invoice-2\">\n" +
                "    <cbc:ID>%s-%s</cbc:ID>\n" +
                "    <cbc:IssueDate>%s</cbc:IssueDate>\n" +
                "    <cac:Signature>\n" +
                "        <cbc:ID>SignFactos</cbc:ID>\n" +
                "        <cac:SignatoryParty>\n" +
                "            <cac:PartyIdentification><cbc:ID>%s</cbc:ID></cac:PartyIdentification>\n" +
                "        </cac:SignatoryParty>\n" +
                "        <cac:DigitalSignature>\n" +
                "            <cbc:SignatureValue>MOCK_SIGNATURE_VALUE_BASE64_ABC123</cbc:SignatureValue>\n" +
                "        </cac:DigitalSignature>\n" +
                "    </cac:Signature>\n" +
                "    <cbc:DocumentCurrencyCode>%s</cbc:DocumentCurrencyCode>\n" +
                "    <cbc:PayableAmount>%s</cbc:PayableAmount>\n" +
                "</Invoice>",
                cpe.getSeries(),
                cpe.getCorrelative(),
                cpe.getIssueDate().toString(),
                cpe.getIssuerRuc().value(),
                cpe.getTotals().totalAmount().currency(),
                cpe.getTotals().totalAmount().amount().toString()
        );
        return xml.getBytes(StandardCharsets.UTF_8);
    }

    private byte[] createMockCdrZip(String series, String correlative) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            String cdrXml = String.format(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ApplicationResponse>\n" +
                    "    <ResponseCode>0</ResponseCode>\n" +
                    "    <Description>El comprobante %s-%s ha sido aceptado</Description>\n" +
                    "</ApplicationResponse>",
                    series, correlative
            );

            ZipEntry entry = new ZipEntry(String.format("R-%s-%s.xml", series, correlative));
            zos.putNextEntry(entry);
            zos.write(cdrXml.getBytes(StandardCharsets.UTF_8));
            zos.closeEntry();
            zos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new BusinessException("Failed to generate simulated CDR ZIP: " + e.getMessage());
        }
    }
}
