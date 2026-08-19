package pe.factos.billing.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.factos.billing.application.dto.EmitCpeCommand;
import pe.factos.billing.application.dto.CpeResponse;
import pe.factos.billing.application.dto.ItemCommand;
import pe.factos.billing.domain.event.CpeAccepted;
import pe.factos.billing.domain.event.CpeEmitted;
import pe.factos.billing.domain.event.CpeRejected;
import pe.factos.billing.domain.model.Cpe;
import pe.factos.billing.domain.port.*;
import pe.factos.issuer.domain.model.AuthorizedSeries;
import pe.factos.issuer.domain.model.DigitalCertificate;
import pe.factos.issuer.domain.model.Issuer;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.issuer.domain.port.IssuerRepository;
import pe.factos.shared.domain.BusinessException;
import pe.factos.shared.domain.DomainEvent;
import pe.factos.shared.domain.DomainEventPublisher;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmitCpeUseCaseTest {

    @Mock private IssuerRepository issuerRepository;
    @Mock private CpeRepository cpeRepository;
    @Mock private FiscalGateway fiscalGateway;
    @Mock private CorrelativeGenerator correlativeGenerator;
    @Mock private StorageGateway storageGateway;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks
    private EmitCpeUseCase useCase;

    private Issuer validIssuer;
    private DigitalCertificate validCertificate;

    @BeforeEach
    void setUp() {
        validCertificate = new DigitalCertificate(
                "base64DummyContent",
                "password",
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(1, ChronoUnit.DAYS)
        );
        validIssuer = new Issuer(
                new Ruc("20123456789"),
                "Corporacion Factos S.A.C.",
                "Av. Lima 123",
                "150101",
                validCertificate
        );
        validIssuer.addAuthorizedSeries(new AuthorizedSeries("F001"));
        validIssuer.addAuthorizedSeries(new AuthorizedSeries("B001"));
    }

    @Test
    void shouldEmitCpeSuccessfullyWhenSunatAccepts() {
        EmitCpeCommand command = new EmitCpeCommand(
                "20123456789",
                "F001",
                "01",
                "20987654321",
                "Cliente Destino S.A.",
                LocalDate.now(),
                List.of(new ItemCommand("PROD01", "Laptop", BigDecimal.ONE, new BigDecimal("1000.00"), "10"))
        );

        when(issuerRepository.findByRuc(any())).thenReturn(Optional.of(validIssuer));
        when(correlativeGenerator.generateNext(any(), any())).thenReturn("00000001");
        
        byte[] signedXml = "<xml>signed</xml>".getBytes();
        byte[] cdrZip = "<xml>cdr</xml>".getBytes();
        CpeSubmissionResult mockResult = CpeSubmissionResult.success("La Factura F001-00000001 ha sido aceptada", signedXml, cdrZip);
        when(fiscalGateway.submit(any(Cpe.class))).thenReturn(mockResult);

        CpeResponse response = useCase.execute(command);

        assertNotNull(response);
        assertTrue(response.accepted());
        assertEquals("F001", response.series());
        assertEquals("00000001", response.correlative());
        assertEquals("La Factura F001-00000001 ha sido aceptada", response.cdrDescription());

        verify(cpeRepository).save(any(Cpe.class));
        verify(storageGateway).storeXml(eq("F001"), eq("00000001"), eq(signedXml));
        verify(storageGateway).storeCdr(eq("F001"), eq("00000001"), eq(cdrZip));

        ArgumentCaptor<DomainEvent> eventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(eventPublisher, times(2)).publish(eventCaptor.capture());
        
        List<DomainEvent> publishedEvents = eventCaptor.getAllValues();
        assertEquals(2, publishedEvents.size());
        assertTrue(publishedEvents.get(0) instanceof CpeEmitted);
        assertTrue(publishedEvents.get(1) instanceof CpeAccepted);
    }

    @Test
    void shouldEmitCpeAsRejectedWhenSunatRejects() {
        EmitCpeCommand command = new EmitCpeCommand(
                "20123456789",
                "F001",
                "01",
                "20987654321",
                "Cliente Destino S.A.",
                LocalDate.now(),
                List.of(new ItemCommand("PROD01", "Laptop", BigDecimal.ONE, new BigDecimal("1000.00"), "10"))
        );

        when(issuerRepository.findByRuc(any())).thenReturn(Optional.of(validIssuer));
        when(correlativeGenerator.generateNext(any(), any())).thenReturn("00000002");
        
        byte[] signedXml = "<xml>signed</xml>".getBytes();
        CpeSubmissionResult mockResult = CpeSubmissionResult.failure("2017", "RUC del adquiriente no existe", signedXml);
        when(fiscalGateway.submit(any(Cpe.class))).thenReturn(mockResult);

        CpeResponse response = useCase.execute(command);

        assertNotNull(response);
        assertFalse(response.accepted());
        assertEquals("2017", response.errorCode());
        assertEquals("RUC del adquiriente no existe", response.errorMessage());

        verify(cpeRepository).save(any(Cpe.class));
        verify(storageGateway).storeXml(eq("F001"), eq("00000002"), eq(signedXml));
        verify(storageGateway, never()).storeCdr(any(), any(), any());

        ArgumentCaptor<DomainEvent> eventCaptor = ArgumentCaptor.forClass(DomainEvent.class);
        verify(eventPublisher, times(2)).publish(eventCaptor.capture());
        
        List<DomainEvent> publishedEvents = eventCaptor.getAllValues();
        assertEquals(2, publishedEvents.size());
        assertTrue(publishedEvents.get(0) instanceof CpeEmitted);
        assertTrue(publishedEvents.get(1) instanceof CpeRejected);
    }

    @Test
    void shouldThrowExceptionWhenIssuerNotFound() {
        EmitCpeCommand command = new EmitCpeCommand(
                "20999999999",
                "F001",
                "01",
                "20987654321",
                "Cliente Destino S.A.",
                LocalDate.now(),
                List.of(new ItemCommand("PROD01", "Laptop", BigDecimal.ONE, new BigDecimal("1000.00"), "10"))
        );

        when(issuerRepository.findByRuc(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowExceptionWhenCertificateExpired() {
        DigitalCertificate expiredCertificate = new DigitalCertificate(
                "base64DummyContent",
                "password",
                Instant.now().minus(5, ChronoUnit.DAYS),
                Instant.now().minus(2, ChronoUnit.DAYS)
        );
        Issuer expiredIssuer = new Issuer(
                new Ruc("20123456789"),
                "Corporacion Factos S.A.C.",
                "Av. Lima 123",
                "150101",
                expiredCertificate
        );

        EmitCpeCommand command = new EmitCpeCommand(
                "20123456789",
                "F001",
                "01",
                "20987654321",
                "Cliente Destino S.A.",
                LocalDate.now(),
                List.of(new ItemCommand("PROD01", "Laptop", BigDecimal.ONE, new BigDecimal("1000.00"), "10"))
        );

        when(issuerRepository.findByRuc(any())).thenReturn(Optional.of(expiredIssuer));

        assertThrows(BusinessException.class, () -> useCase.execute(command));
    }

    @Test
    void shouldThrowExceptionWhenSeriesNotAuthorized() {
        EmitCpeCommand command = new EmitCpeCommand(
                "20123456789",
                "F999",
                "01",
                "20987654321",
                "Cliente Destino S.A.",
                LocalDate.now(),
                List.of(new ItemCommand("PROD01", "Laptop", BigDecimal.ONE, new BigDecimal("1000.00"), "10"))
        );

        when(issuerRepository.findByRuc(any())).thenReturn(Optional.of(validIssuer));

        assertThrows(BusinessException.class, () -> useCase.execute(command));
    }
}
