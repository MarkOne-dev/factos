package pe.factos.billing.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.issuer.infrastructure.persistence.IssuerEntity;
import pe.factos.issuer.infrastructure.persistence.SpringDataIssuerRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class JpaCorrelativeGeneratorAdapterTest extends BasePersistenceTest {

    @Autowired
    private JpaCorrelativeGeneratorAdapter correlativeGenerator;

    @Autowired
    private SpringDataCpeRepository cpeRepository;

    @Autowired
    private SpringDataIssuerRepository issuerRepository;

    @Test
    void shouldReturnDefaultCorrelativeWhenNoCpeExists() {
        // Arrange
        Ruc issuerRuc = new Ruc("20555555555");
        String series = "F001";

        // Act
        String next = correlativeGenerator.generateNext(issuerRuc, series);

        // Assert
        assertThat(next).isEqualTo("00000001");
    }

    @Test
    void shouldIncrementAndFormatMaxCorrelative() {
        // Arrange
        String rucStr = "20666666666";
        Ruc issuerRuc = new Ruc(rucStr);
        String series = "F001";

        // Create Issuer first to satisfy foreign key constraint
        IssuerEntity issuerEntity = new IssuerEntity();
        issuerEntity.setRuc(rucStr);
        issuerEntity.setCorporateName("Test Issuer");
        issuerEntity.setAuthorizedSeries(new HashSet<>(Collections.singletonList("F001")));
        issuerRepository.save(issuerEntity);

        // Save a CPE with correlative "00000045"
        CpeEntity cpeEntity = new CpeEntity();
        cpeEntity.setIssuerRuc(rucStr);
        cpeEntity.setSeries(series);
        cpeEntity.setCorrelative("00000045");
        cpeEntity.setCpeType("01");
        cpeEntity.setIssueDate(LocalDate.now());
        cpeEntity.setTotalTaxable(BigDecimal.ZERO);
        cpeEntity.setTotalExonerated(BigDecimal.ZERO);
        cpeEntity.setTotalInactive(BigDecimal.ZERO);
        cpeEntity.setTotalIgv(BigDecimal.ZERO);
        cpeEntity.setTotalFree(BigDecimal.ZERO);
        cpeEntity.setTotalAmount(BigDecimal.ZERO);
        cpeEntity.setCurrency("PEN");
        cpeEntity.setState("PENDING");
        cpeEntity.setItems(Collections.emptyList());

        cpeRepository.save(cpeEntity);

        // Act
        String next = correlativeGenerator.generateNext(issuerRuc, series);

        // Assert
        assertThat(next).isEqualTo("00000046");
    }
}
