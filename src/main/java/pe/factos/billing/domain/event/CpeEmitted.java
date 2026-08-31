package pe.factos.billing.domain.event;

import pe.factos.shared.domain.DomainEvent;

import java.time.Instant;

public record CpeEmitted(
        String series,
        String correlative,
        String issuerRuc,
        Instant occurredOn
) implements DomainEvent {
    public CpeEmitted(String series, String correlative, String issuerRuc) {
        this(series, correlative, issuerRuc, Instant.now());
    }
}
