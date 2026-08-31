package pe.factos.security.interfaces.rest.resources;

import java.time.Instant;

public record ApiKeyResource(
        String keyValue,
        String clientName,
        Instant expiresAt,
        boolean active
) {
}
