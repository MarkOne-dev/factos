package pe.factos.issuer.domain.model;

import org.junit.jupiter.api.Test;
import pe.factos.shared.domain.BusinessException;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizedSeriesTest {

    @Test
    void shouldCreateAuthorizedSeriesWhenValid() {
        AuthorizedSeries invoiceSeries = new AuthorizedSeries("F001");
        assertEquals("F001", invoiceSeries.code());
        assertTrue(invoiceSeries.isInvoiceSeries());
        assertFalse(invoiceSeries.isBillSeries());

        AuthorizedSeries billSeries = new AuthorizedSeries("B123");
        assertEquals("B123", billSeries.code());
        assertTrue(billSeries.isBillSeries());
        assertFalse(billSeries.isInvoiceSeries());
    }

    @Test
    void shouldThrowExceptionWhenSeriesIsEmpty() {
        assertThrows(BusinessException.class, () -> new AuthorizedSeries(""));
        assertThrows(BusinessException.class, () -> new AuthorizedSeries(null));
    }

    @Test
    void shouldThrowExceptionWhenSeriesIsInvalidLength() {
        assertThrows(BusinessException.class, () -> new AuthorizedSeries("F01"));
        assertThrows(BusinessException.class, () -> new AuthorizedSeries("F0001"));
    }

    @Test
    void shouldThrowExceptionWhenSeriesHasInvalidPrefix() {
        assertThrows(BusinessException.class, () -> new AuthorizedSeries("T001"));
        assertThrows(BusinessException.class, () -> new AuthorizedSeries("A001"));
    }

    @Test
    void shouldThrowExceptionWhenSeriesHasSpecialCharacters() {
        assertThrows(BusinessException.class, () -> new AuthorizedSeries("F0-1"));
    }
}
