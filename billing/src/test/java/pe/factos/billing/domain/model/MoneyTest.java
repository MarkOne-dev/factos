package pe.factos.billing.domain.model;

import org.junit.jupiter.api.Test;
import pe.factos.shared.domain.BusinessException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyAndSetScale() {
        Money money = new Money(new BigDecimal("10.555"), "PEN");
        assertEquals(new BigDecimal("10.56"), money.amount());
        assertEquals("PEN", money.currency());

        Money money2 = Money.of(100.5);
        assertEquals(new BigDecimal("100.50"), money2.amount());
        assertEquals("PEN", money2.currency());
    }

    @Test
    void shouldAddMoneyWithSameCurrency() {
        Money m1 = Money.of(10.25);
        Money m2 = Money.of(5.50);
        Money result = m1.add(m2);

        assertEquals(new BigDecimal("15.75"), result.amount());
    }

    @Test
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {
        Money m1 = Money.of(10.25);
        Money m2 = new Money(BigDecimal.TEN, "USD");

        assertThrows(BusinessException.class, () -> m1.add(m2));
    }

    @Test
    void shouldSubtractMoney() {
        Money m1 = Money.of(10.25);
        Money m2 = Money.of(5.50);
        Money result = m1.subtract(m2);

        assertEquals(new BigDecimal("4.75"), result.amount());
    }

    @Test
    void shouldMultiplyMoney() {
        Money m = Money.of(10.20);
        Money result = m.multiply(2.5);

        assertEquals(new BigDecimal("25.50"), result.amount());
    }

    @Test
    void shouldDivideMoney() {
        Money m = Money.of(10.00);
        Money result = m.divide(3);

        assertEquals(new BigDecimal("3.33"), result.amount());
    }

    @Test
    void shouldCompareMoney() {
        Money m1 = Money.of(10.00);
        Money m2 = Money.of(20.00);

        assertTrue(m2.isGreaterThan(m1));
        assertTrue(m1.isLessThan(m2));
        assertFalse(m1.isGreaterThan(m2));
        assertTrue(Money.ZERO.isZero());
    }
}
