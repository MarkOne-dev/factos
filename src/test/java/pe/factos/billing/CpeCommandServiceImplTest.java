package pe.factos.billing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.factos.billing.application.internal.commandservices.CpeCommandServiceImpl;
import pe.factos.billing.domain.model.aggregates.Cpe;
import pe.factos.billing.domain.model.commands.EmitCpeCommand;
import pe.factos.billing.domain.model.commands.ItemCommand;
import pe.factos.billing.domain.repositories.CpeRepository;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.issuer.domain.repositories.IssuerRepository;
import pe.factos.shared.application.result.Result;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CpeCommandServiceImplTest {

    @Mock
    private CpeRepository cpeRepository;

    @Mock
    private IssuerRepository issuerRepository;

    private CpeCommandServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new CpeCommandServiceImpl(cpeRepository, issuerRepository);
    }

    @Test
    @DisplayName("Should successfully calculate taxable base and IGV 18% for GRAVADO items")
    void shouldCalculateTaxableAndIgvForGravadoItem() {
        String issuerRuc = "20123456789";
        when(issuerRepository.existsByRuc(any(Ruc.class))).thenReturn(true);
        when(cpeRepository.existsBySeriesAndCorrelative("F001", "00000001")).thenReturn(false);
        when(cpeRepository.save(any(Cpe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCommand item = new ItemCommand(
                "P001",
                "Producto Gravado",
                BigDecimal.valueOf(1),
                BigDecimal.valueOf(118.00),
                "10" // GRAVADO
        );

        EmitCpeCommand command = new EmitCpeCommand(
                "F001",
                "00000001",
                "01",
                LocalDate.now(),
                issuerRuc,
                "20987654321",
                "Cliente Test",
                List.of(item),
                "PEN"
        );

        Result<Cpe, ?> result = service.handle(command);

        assertThat(result.isSuccess()).isTrue();
        Cpe cpe = result.success().get();

        assertThat(cpe.getTotals().totalAmount().amount()).isEqualByComparingTo("118.00");
        assertThat(cpe.getTotals().totalTaxable().amount()).isEqualByComparingTo("100.00");
        assertThat(cpe.getTotals().totalIgv().amount()).isEqualByComparingTo("18.00");
        assertThat(cpe.getTotals().totalExonerated().amount()).isEqualByComparingTo("0.00");
    }

    @Test
    @DisplayName("Should calculate 0 IGV and totalExonerated for EXONERADO items")
    void shouldCalculateZeroIgvForExoneradoItem() {
        String issuerRuc = "20123456789";
        when(issuerRepository.existsByRuc(any(Ruc.class))).thenReturn(true);
        when(cpeRepository.existsBySeriesAndCorrelative("F001", "00000002")).thenReturn(false);
        when(cpeRepository.save(any(Cpe.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ItemCommand item = new ItemCommand(
                "P002",
                "Producto Exonerado",
                BigDecimal.valueOf(2),
                BigDecimal.valueOf(50.00),
                "20" // EXONERADO
        );

        EmitCpeCommand command = new EmitCpeCommand(
                "F001",
                "00000002",
                "01",
                LocalDate.now(),
                issuerRuc,
                "20987654321",
                "Cliente Test",
                List.of(item),
                "PEN"
        );

        Result<Cpe, ?> result = service.handle(command);

        assertThat(result.isSuccess()).isTrue();
        Cpe cpe = result.success().get();

        assertThat(cpe.getTotals().totalAmount().amount()).isEqualByComparingTo("100.00");
        assertThat(cpe.getTotals().totalIgv().amount()).isEqualByComparingTo("0.00");
        assertThat(cpe.getTotals().totalExonerated().amount()).isEqualByComparingTo("100.00");
    }
}
