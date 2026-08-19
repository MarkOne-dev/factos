package pe.factos.billing.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.factos.billing.domain.model.*;
import pe.factos.billing.domain.port.CpeRepository;
import pe.factos.issuer.domain.model.Ruc;
import pe.factos.shared.domain.BusinessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueryCpeUseCaseTest {

    @Mock private CpeRepository cpeRepository;

    @InjectMocks
    private QueryCpeUseCase useCase;

    @Test
    void shouldFindCpeSuccessfully() {
        Ruc issuerRuc = new Ruc("20123456789");
        Money value = Money.of(100.00);
        Item item = new Item(
                "P001", "Product", BigDecimal.ONE, value, value.multiply(1.18),
                pe.factos.catalog.domain.IgvAffectationType.TAXABLE_ONEROUS, value, value.multiply(0.18), value.multiply(1.18)
        );
        CpeTotals totals = new CpeTotals(value, Money.ZERO, Money.ZERO, value.multiply(0.18), Money.ZERO, value.multiply(1.18));
        Invoice invoice = new Invoice(
                "F001", "00000001", LocalDate.now(), issuerRuc,
                "20987654321", "Client S.A.", List.of(item), totals
        );

        when(cpeRepository.findBySeriesAndCorrelative("F001", "00000001"))
                .thenReturn(Optional.of(invoice));

        Cpe result = useCase.execute("F001", "00000001");

        assertNotNull(result);
        assertEquals(invoice, result);
        verify(cpeRepository).findBySeriesAndCorrelative("F001", "00000001");
    }

    @Test
    void shouldThrowExceptionWhenCpeNotFound() {
        when(cpeRepository.findBySeriesAndCorrelative("F001", "99999999"))
                .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> useCase.execute("F001", "99999999"));
    }

    @Test
    void shouldThrowExceptionWhenParametersAreInvalid() {
        assertThrows(BusinessException.class, () -> useCase.execute("", "00000001"));
        assertThrows(BusinessException.class, () -> useCase.execute("F001", null));
    }
}
