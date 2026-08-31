package pe.factos.issuer.domain.model.valueobjects;

import pe.factos.shared.domain.BusinessException;

import java.time.LocalDate;

public record DigitalCertificate(String alias, String password, byte[] pfxContent, LocalDate validUntil) {
    public DigitalCertificate {
        if (alias == null || alias.isBlank()) {
            throw new BusinessException("Certificate alias cannot be empty");
        }
        if (validUntil != null && validUntil.isBefore(LocalDate.now())) {
            throw new BusinessException("Certificate has expired on " + validUntil);
        }
    }

    public boolean isValid() {
        return validUntil == null || !validUntil.isBefore(LocalDate.now());
    }

    public boolean isExpired(java.time.Instant now) {
        if (validUntil == null) return false;
        java.time.LocalDate nowDate = java.time.LocalDate.ofInstant(now, java.time.ZoneId.systemDefault());
        return validUntil.isBefore(nowDate);
    }
}
