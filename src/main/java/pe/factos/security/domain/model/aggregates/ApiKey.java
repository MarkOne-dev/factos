package pe.factos.security.domain.model.aggregates;

import lombok.Getter;
import pe.factos.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ApiKey extends AbstractDomainAggregateRoot<ApiKey> {
    private final String keyValue;
    private final String clientName;
    private final Instant expiresAt;
    private boolean active;
    private transient String rawKey;

    public ApiKey(String keyValue, String clientName, Instant expiresAt, boolean active) {
        this.keyValue = keyValue;
        this.clientName = clientName;
        this.expiresAt = expiresAt;
        this.active = active;
        this.rawKey = null;
    }

    public ApiKey(String keyValue, String rawKey, String clientName, Instant expiresAt, boolean active) {
        this.keyValue = keyValue;
        this.rawKey = rawKey;
        this.clientName = clientName;
        this.expiresAt = expiresAt;
        this.active = active;
    }

    public ApiKey(String clientName, Instant expiresAt) {
        this(UUID.randomUUID().toString().replace("-", ""), clientName, expiresAt, true);
    }

    public boolean isValid() {
        return active && (expiresAt == null || expiresAt.isAfter(Instant.now()));
    }

    public void deactivate() {
        this.active = false;
    }
}
