package pe.factos.billing.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ItemResource(
        @NotBlank(message = "Código de ítem es requerido")
        String code,

        @NotBlank(message = "Descripción de ítem es requerida")
        String description,

        @NotNull(message = "Cantidad es requerida")
        @DecimalMin(value = "0.0001", message = "Cantidad debe ser mayor a 0")
        BigDecimal quantity,

        @NotNull(message = "Precio unitario es requerido")
        @DecimalMin(value = "0.01", message = "Precio unitario debe ser mayor a 0")
        BigDecimal unitPrice,

        String affectationType
) {
}
