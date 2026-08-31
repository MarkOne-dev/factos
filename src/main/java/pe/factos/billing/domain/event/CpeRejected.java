package pe.factos.billing.domain.event;

import pe.factos.shared.domain.DomainEvent;

import java.time.Instant;

public record CpeRejected(
        String series,
        String correlative,
        String issuerRuc,
        String errorCode,
        String errorMessage,
        Instant occurredOn
) implements DomainEvent {
    public CpeRejected(String series, String correlative, String issuerRuc, String errorCode, String errorMessage) {
        this(series, correlative, issuerRuc, errorCode, errorMessage, Instant.now());
    }
}
