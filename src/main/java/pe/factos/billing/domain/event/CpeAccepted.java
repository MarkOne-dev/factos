package pe.factos.billing.domain.event;

import pe.factos.shared.domain.DomainEvent;

import java.time.Instant;

public record CpeAccepted(
        String series,
        String correlative,
        String issuerRuc,
        String cdrDescription,
        Instant occurredOn
) implements DomainEvent {
    public CpeAccepted(String series, String correlative, String issuerRuc, String cdrDescription) {
        this(series, correlative, issuerRuc, cdrDescription, Instant.now());
    }
}
