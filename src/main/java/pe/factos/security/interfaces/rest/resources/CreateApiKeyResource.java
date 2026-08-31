package pe.factos.security.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateApiKeyResource(
        @NotBlank(message = "Nombre de cliente es requerido")
        String clientName,
        Integer validDays
) {
}
