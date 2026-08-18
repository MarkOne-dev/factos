package pe.factos.billing.domain.model;

import java.time.LocalDate;

public final class Boleta implements Comprobante {
    private final String serie;
    private final String correlativo;
    private final LocalDate fechaEmision;
    private final String rucEmisor;
    private final String documentoAdquiriente;

    public Boleta(String serie, String correlativo, LocalDate fechaEmision, String rucEmisor, String documentoAdquiriente) {
        this.serie = serie;
        this.correlativo = correlativo;
        this.fechaEmision = fechaEmision;
        this.rucEmisor = rucEmisor;
        this.documentoAdquiriente = documentoAdquiriente;
    }

    @Override
    public String getSerie() { return serie; }

    @Override
    public String getCorrelativo() { return correlativo; }

    @Override
    public LocalDate getFechaEmision() { return fechaEmision; }

    @Override
    public String getRucEmisor() { return rucEmisor; }

    @Override
    public String getDocumentoAdquiriente() { return documentoAdquiriente; }
}
