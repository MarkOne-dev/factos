package pe.factos.billing.domain.model.valueobjects;

import pe.factos.shared.domain.BusinessException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Money(BigDecimal amount, String currency) {
    public static final String DEFAULT_CURRENCY = "PEN";
    public static final Money ZERO = new Money(BigDecimal.ZERO, DEFAULT_CURRENCY);

    public Money(BigDecimal amount, String currency) {
        if (amount == null) {
            throw new BusinessException("Amount cannot be null");
        }
        if (currency == null || currency.isBlank()) {
            throw new BusinessException("Currency cannot be empty");
        }
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        this.currency = currency.toUpperCase();
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount, DEFAULT_CURRENCY);
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount), DEFAULT_CURRENCY);
    }

    public Money add(Money other) {
        validateMatchingCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateMatchingCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public Money multiply(BigDecimal factor) {
        if (factor == null) {
            throw new BusinessException("Factor cannot be null");
        }
        return new Money(this.amount.multiply(factor), this.currency);
    }

    public Money multiply(double factor) {
        return multiply(BigDecimal.valueOf(factor));
    }

    public Money divide(BigDecimal divisor) {
        if (divisor == null) {
            throw new BusinessException("Divisor cannot be null");
        }
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("Cannot divide by zero");
        }
        return new Money(this.amount.divide(divisor, 4, RoundingMode.HALF_UP), this.currency);
    }

    public Money divide(double divisor) {
        return divide(BigDecimal.valueOf(divisor));
    }

    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isGreaterThan(Money other) {
        validateMatchingCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }

    public boolean isLessThan(Money other) {
        validateMatchingCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }

    private void validateMatchingCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new BusinessException("Currencies do not match: " + this.currency + " vs " + other.currency);
        }
    }
}
