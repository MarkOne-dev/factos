package pe.factos.issuer.domain.model;

import pe.factos.shared.domain.BusinessException;

import java.time.Instant;

public record DigitalCertificate(String base64Content, String password, Instant validFrom, Instant validTo) {
    public DigitalCertificate {
        if (base64Content == null || base64Content.isBlank()) {
            throw new BusinessException("Certificate content cannot be empty");
        }
        if (password == null || password.isBlank()) {
            throw new BusinessException("Certificate password cannot be empty");
        }
        if (validFrom == null || validTo == null) {
            throw new BusinessException("Certificate validity dates cannot be null");
        }
        if (validFrom.isAfter(validTo)) {
            throw new BusinessException("Certificate validFrom date cannot be after validTo date");
        }
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(validTo) || now.isBefore(validFrom);
    }
}
