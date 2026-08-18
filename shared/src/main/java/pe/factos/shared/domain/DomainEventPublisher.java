package pe.factos.shared.domain;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
