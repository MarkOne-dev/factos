package pe.factos.billing.domain.model;

import java.time.LocalDate;

public sealed interface Comprobante permits Factura, Boleta {
    String getSerie();
    String getCorrelativo();
    LocalDate getFechaEmision();
    String getRucEmisor();
    String getDocumentoAdquiriente();
}
