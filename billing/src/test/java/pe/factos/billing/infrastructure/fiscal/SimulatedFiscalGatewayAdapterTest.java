package pe.factos.billing.infrastructure.fiscal;

import org.junit.jupiter.api.Test;
import pe.factos.billing.domain.model.*;
import pe.factos.catalog.domain.IgvAffectationType;
import pe.factos.billing.domain.port.CpeSubmissionResult;
import pe.factos.issuer.domain.model.Ruc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SimulatedFiscalGatewayAdapterTest {

    private final SimulatedFiscalGatewayAdapter fiscalGateway = new SimulatedFiscalGatewayAdapter();

    @Test
    void shouldSubmitAndReturnSuccessCDR() {
        // Arrange
        Cpe cpe = createMockCpe("F001", "00000001");

        // Act
        CpeSubmissionResult result = fiscalGateway.submit(cpe);

        // Assert
        assertThat(result.accepted()).isTrue();
        assertThat(result.cdrDescription()).contains("F001-00000001 ha sido aceptado");
        assertThat(result.cdrZip()).isNotEmpty();
        assertThat(result.signedXml()).isNotEmpty();
        assertThat(result.errorCode()).isNull();
        assertThat(result.errorMessage()).isNull();
    }

    @Test
    void shouldSubmitAndReturnFailureCDRForCorrelative99() {
        // Arrange
        Cpe cpe = createMockCpe("F001", "00000099");

        // Act
        CpeSubmissionResult result = fiscalGateway.submit(cpe);

        // Assert
        assertThat(result.accepted()).isFalse();
        assertThat(result.errorCode()).isEqualTo("1034");
        assertThat(result.errorMessage()).contains("ya existe");
        assertThat(result.signedXml()).isNotEmpty();
        assertThat(result.cdrZip()).isNull();
        assertThat(result.cdrDescription()).isNull();
    }

    private Cpe createMockCpe(String series, String correlative) {
        Ruc issuerRuc = new Ruc("20123456789");
        Money money = new Money(BigDecimal.valueOf(118.00), "PEN");
        Item item = new Item(
                "P001", "Product", BigDecimal.ONE,
                new Money(BigDecimal.valueOf(100.00), "PEN"),
                money, IgvAffectationType.TAXABLE_ONEROUS,
                new Money(BigDecimal.valueOf(100.00), "PEN"), new Money(BigDecimal.valueOf(18.00), "PEN"),
                money
        );
        CpeTotals totals = new CpeTotals(
                new Money(BigDecimal.valueOf(100.00), "PEN"),
                Money.ZERO, Money.ZERO, new Money(BigDecimal.valueOf(18.00), "PEN"),
                Money.ZERO, money
        );
        return new Invoice(series, correlative, LocalDate.now(), issuerRuc, "20987654321", "Client", List.of(item), totals);
    }
}
