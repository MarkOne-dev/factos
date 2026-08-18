package pe.factos.issuer.domain.model;

import org.junit.jupiter.api.Test;
import pe.factos.shared.domain.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

class RucTest {

    @Test
    void shouldCreateRucWhenValid() {
        Ruc ruc = new Ruc("20123456789");
        assertEquals("20123456789", ruc.value());
        
        Ruc ruc2 = new Ruc("10987654321");
        assertEquals("10987654321", ruc2.value());
    }

    @Test
    void shouldThrowExceptionWhenRucIsEmpty() {
        assertThrows(BusinessException.class, () -> new Ruc(""));
        assertThrows(BusinessException.class, () -> new Ruc(null));
    }

    @Test
    void shouldThrowExceptionWhenRucIsInvalidLength() {
        assertThrows(BusinessException.class, () -> new Ruc("2012345678"));
        assertThrows(BusinessException.class, () -> new Ruc("201234567890"));
    }

    @Test
    void shouldThrowExceptionWhenRucHasInvalidPrefix() {
        assertThrows(BusinessException.class, () -> new Ruc("30123456789"));
        assertThrows(BusinessException.class, () -> new Ruc("11123456789"));
    }

    @Test
    void shouldThrowExceptionWhenRucContainsLetters() {
        assertThrows(BusinessException.class, () -> new Ruc("2012345678A"));
    }
}
