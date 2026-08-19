package pe.factos.billing.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pe.factos.billing.domain.model.Bill;
import pe.factos.billing.domain.model.Cpe;
import pe.factos.billing.domain.model.CpeTotals;
import pe.factos.billing.domain.model.Invoice;
import pe.factos.billing.domain.model.Item;
import pe.factos.billing.domain.model.Money;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.issuer.domain.model.AuthorizedSeries;
import pe.factos.issuer.domain.model.DigitalCertificate;
import pe.factos.issuer.domain.model.Issuer;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.issuer.infrastructure.persistence.JpaIssuerRepositoryAdapter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JpaPersistenceIntegrationTest extends BasePersistenceTest {

    @Autowired
    private JpaIssuerRepositoryAdapter issuerRepository;

    @Autowired
    private JpaCpeRepositoryAdapter cpeRepository;

    @Test
    void shouldSaveAndFindIssuer() {
        Ruc ruc = new Ruc("20987654321");
        DigitalCertificate certificate = new DigitalCertificate(
                "base64CertificateData",
                "certPassword",
                Instant.now().minus(1, ChronoUnit.DAYS),
                Instant.now().plus(30, ChronoUnit.DAYS)
        );
        Issuer issuer = new Issuer(
                ruc,
                "Empresa Peruana S.A.C.",
                "Calle Real 456",
                "150101",
                certificate
        );
        issuer.addAuthorizedSeries(new AuthorizedSeries("F001"));
        issuer.addAuthorizedSeries(new AuthorizedSeries("B001"));

        issuerRepository.save(issuer);
        Optional<Issuer> found = issuerRepository.findByRuc(ruc);

        assertTrue(found.isPresent());
        Issuer loaded = found.get();
        assertEquals("Empresa Peruana S.A.C.", loaded.getCorporateName());
        assertEquals("Calle Real 456", loaded.getAddress());
        assertEquals("150101", loaded.getUbigeo());
        assertNotNull(loaded.getCertificate());
        assertEquals("base64CertificateData", loaded.getCertificate().base64Content());
        assertTrue(loaded.isSeriesAuthorized("F001"));
        assertTrue(loaded.isSeriesAuthorized("B001"));
        assertFalse(loaded.isSeriesAuthorized("F002"));
    }

    @Test
    void shouldSaveAndFindInvoiceCpe() {
        Ruc issuerRuc = new Ruc("20123456789");
        Issuer issuer = new Issuer(
                issuerRuc,
                "Corporacion Factores S.A.",
                "Calle Comercio 123",
                "150101",
                null
        );
        issuerRepository.save(issuer);

        Money value = Money.of(100.00);
        Item item = new Item(
                "P001",
                "Consultoria TI",
                BigDecimal.ONE,
                value,
                value.multiply(1.18),
                IgvAffectationType.TAXABLE_ONEROUS,
                value,
                value.multiply(0.18),
                value.multiply(1.18)
        );
        CpeTotals totals = new CpeTotals(
                value,
                Money.ZERO,
                Money.ZERO,
                value.multiply(0.18),
                Money.ZERO,
                value.multiply(1.18)
        );
        Cpe invoice = new Invoice(
                "F001",
                "00001234",
                LocalDate.now(),
                issuerRuc,
                "20444444444",
                "Cliente Internacional S.A.",
                List.of(item),
                totals
        );

        cpeRepository.save(invoice);
        Optional<Cpe> found = cpeRepository.findBySeriesAndCorrelative("F001", "00001234");

        assertTrue(found.isPresent());
        Cpe loaded = found.get();
        assertTrue(loaded instanceof Invoice);
        assertEquals("F001", loaded.getSeries());
        assertEquals("00001234", loaded.getCorrelative());
        assertEquals("20444444444", loaded.getAcquirerDocument());
        assertEquals("Cliente Internacional S.A.", loaded.getAcquirerName());
        assertEquals(new BigDecimal("118.00"), loaded.getTotals().totalAmount().amount());
        
        assertEquals(1, loaded.getItems().size());
        Item loadedItem = loaded.getItems().get(0);
        assertEquals("P001", loadedItem.getCode());
        assertEquals("Consultoria TI", loadedItem.getDescription());
        assertEquals(BigDecimal.ONE.setScale(4), loadedItem.getQuantity());
        assertEquals(new BigDecimal("18.00"), loadedItem.getIgv().amount());
    }

    @Test
    void shouldSaveAndFindBillCpe() {
        Ruc issuerRuc = new Ruc("20777777777");
        Issuer issuer = new Issuer(
                issuerRuc,
                "Supermercados Lima S.A.",
                "Av. Arequipa 999",
                "150101",
                null
        );
        issuerRepository.save(issuer);

        Money value = Money.of(10.00);
        Item item = new Item(
                "P002",
                "Aceite de Oliva",
                BigDecimal.valueOf(2),
                value,
                value.multiply(1.18),
                IgvAffectationType.TAXABLE_ONEROUS,
                value.multiply(2),
                value.multiply(2).multiply(0.18),
                value.multiply(2).multiply(1.18)
        );
        CpeTotals totals = new CpeTotals(
                value.multiply(2),
                Money.ZERO,
                Money.ZERO,
                value.multiply(2).multiply(0.18),
                Money.ZERO,
                value.multiply(2).multiply(1.18)
        );
        Cpe bill = new Bill(
                "B001",
                "00005678",
                LocalDate.now(),
                issuerRuc,
                "45678912",
                "Pedro Perez",
                List.of(item),
                totals
        );

        cpeRepository.save(bill);
        Optional<Cpe> found = cpeRepository.findBySeriesAndCorrelative("B001", "00005678");

        assertTrue(found.isPresent());
        Cpe loaded = found.get();
        assertTrue(loaded instanceof Bill);
        assertEquals("B001", loaded.getSeries());
        assertEquals("00005678", loaded.getCorrelative());
        assertEquals("45678912", loaded.getAcquirerDocument());
        assertEquals("Pedro Perez", loaded.getAcquirerName());
        assertEquals(new BigDecimal("23.60"), loaded.getTotals().totalAmount().amount());
    }
}
