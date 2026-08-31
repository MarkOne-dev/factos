package pe.factos.issuer.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateIssuerResource(
        @NotBlank(message = "RUC es requerido")
        @Pattern(regexp = "^(10|20|15|17)\\d{9}$", message = "RUC debe tener 11 dígitos y empezar con 10, 20, 15 o 17")
        String ruc,

        @NotBlank(message = "Razón social es requerida")
        String corporateName,

        String address,

        String ubigeo
) {
}
