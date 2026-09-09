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
        @Pattern(regexp = "^[FB][A-Z0-9]{3}$", message = "Serie debe iniciar con F o B seguido de 3 caracteres alfanuméricos (ej: F001, B001)")
        String series,

        @NotBlank(message = "Correlativo es requerido")
        @Pattern(regexp = "^\\d{1,8}$", message = "Correlativo debe ser numérico de hasta 8 dígitos (ej: 00000001)")
        String correlative,

        @NotBlank(message = "Tipo de comprobante es requerido")
        @Pattern(regexp = "^(01|03|07|08)$", message = "Tipo de comprobante debe ser 01 (Factura), 03 (Boleta), 07 (NC) u 08 (ND)")
        String cpeType,

        @NotNull(message = "Fecha de emisión es requerida")
        LocalDate issueDate,

        @NotBlank(message = "RUC emisor es requerido")
        @Pattern(regexp = "^(10|15|16|17|20)\\d{9}$", message = "RUC emisor debe tener 11 dígitos y empezar con 10, 20, 15, 16 o 17")
        String issuerRuc,

        @NotBlank(message = "Documento del adquiriente es requerido")
        @Pattern(regexp = "^\\d{8,11}$", message = "Documento del adquiriente debe ser numérico entre 8 (DNI) y 11 (RUC) dígitos")
        String acquirerDocument,

        @NotBlank(message = "Nombre del adquiriente es requerido")
        String acquirerName,

        @NotEmpty(message = "Debe incluir al menos un ítem")
        @Valid
        List<ItemResource> items,

        String currency
) {
}
