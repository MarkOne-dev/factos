package pe.factos.issuer.domain.model;

import pe.factos.shared.domain.BusinessException;

import java.util.regex.Pattern;

public record AuthorizedSeries(String code) {
    private static final Pattern SERIES_PATTERN = Pattern.compile("^[FB][A-Z0-9]{3}$");

    public AuthorizedSeries {
        if (code == null || code.isBlank()) {
            throw new BusinessException("Series code cannot be empty");
        }
        if (!SERIES_PATTERN.matcher(code).matches()) {
            throw new BusinessException("Series must be 4 characters, starting with F or B followed by 3 alphanumeric chars: " + code);
        }
    }

    public boolean isInvoiceSeries() {
        return code.startsWith("F");
    }

    public boolean isBillSeries() {
        return code.startsWith("B");
    }
}
