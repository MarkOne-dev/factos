package pe.factos.billing.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;
import java.util.List;

public record EmitCpeResource(
        @NotBlank(message = "Serie es requerida")
        @Pattern(regexp = "^[FFBB]\\d{3}$", message = "Serie debe iniciar con F o B seguido de 3 dígitos (ej: F001, B001)")
        String series,

        @NotBlank(message = "Correlativo es requerido")
        String correlative,

        @NotBlank(message = "Tipo de comprobante es requerido")
        String cpeType,

        @NotNull(message = "Fecha de emisión es requerida")
        LocalDate issueDate,

        @NotBlank(message = "RUC emisor es requerido")
        String issuerRuc,

        @NotBlank(message = "Documento del adquiriente es requerido")
        String acquirerDocument,

        @NotBlank(message = "Nombre del adquiriente es requerido")
        String acquirerName,

        @NotEmpty(message = "Debe incluir al menos un ítem")
        @Valid
        List<ItemResource> items,

        String currency
) {
}
