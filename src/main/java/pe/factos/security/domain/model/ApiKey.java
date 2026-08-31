package pe.factos.security.domain.model;

import java.time.Instant;

public record ApiKey(String key, String clientName, Instant expiresAt, boolean active) {
    public boolean isValid() {
        return active && (expiresAt == null || expiresAt.isAfter(Instant.now()));
    }
}
