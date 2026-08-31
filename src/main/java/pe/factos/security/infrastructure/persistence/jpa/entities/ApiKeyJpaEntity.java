package pe.factos.security.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.factos.security.domain.model.aggregates.ApiKey;
import pe.factos.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

import java.time.Instant;

@Entity
@Table(name = "api_keys")
@Getter
@Setter
@NoArgsConstructor
public class ApiKeyJpaEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "key_value", nullable = false, unique = true, length = 64)
    private String keyValue;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    public ApiKeyJpaEntity(ApiKey apiKey) {
        this.keyValue = apiKey.getKeyValue();
        this.clientName = apiKey.getClientName();
        this.expiresAt = apiKey.getExpiresAt();
        this.active = apiKey.isActive();
    }

    public ApiKey toDomain() {
        return new ApiKey(keyValue, clientName, expiresAt, active);
    }
}
