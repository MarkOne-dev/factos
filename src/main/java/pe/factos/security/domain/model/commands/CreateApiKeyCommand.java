package pe.factos.security.domain.model.commands;

public record CreateApiKeyCommand(String clientName, Integer validDays) {
}
