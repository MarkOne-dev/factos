package pe.factos.issuer.domain.model.valueobjects;

import pe.factos.shared.domain.BusinessException;

import java.util.regex.Pattern;

public record Ruc(String value) {
    private static final Pattern RUC_PATTERN = Pattern.compile("^(10|20|15|17)\\d{9}$");

    public Ruc {
        if (value == null || value.isBlank()) {
            throw new BusinessException("RUC cannot be empty");
        }
        if (!RUC_PATTERN.matcher(value).matches()) {
            throw new BusinessException("RUC must be 11 digits and start with 10, 20, 15, or 17: " + value);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
