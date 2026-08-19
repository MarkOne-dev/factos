package pe.factos.billing.infrastructure.persistence;

import java.io.Serializable;
import java.util.Objects;

public class CpeId implements Serializable {
    private String issuerRuc;
    private String series;
    private String correlative;

    public CpeId() {}

    public CpeId(String issuerRuc, String series, String correlative) {
        this.issuerRuc = issuerRuc;
        this.series = series;
        this.correlative = correlative;
    }

    public String getIssuerRuc() { return issuerRuc; }
    public void setIssuerRuc(String issuerRuc) { this.issuerRuc = issuerRuc; }

    public String getSeries() { return series; }
    public void setSeries(String series) { this.series = series; }

    public String getCorrelative() { return correlative; }
    public void setCorrelative(String correlative) { this.correlative = correlative; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpeId cpeId = (CpeId) o;
        return Objects.equals(issuerRuc, cpeId.issuerRuc) &&
                Objects.equals(series, cpeId.series) &&
                Objects.equals(correlative, cpeId.correlative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuerRuc, series, correlative);
    }
}
