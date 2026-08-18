package pe.factos.billing.domain.model;

import java.time.LocalDate;

public final class Invoice implements Cpe {
    private final String series;
    private final String correlative;
    private final LocalDate issueDate;
    private final String issuerRuc;
    private final String acquirerDocument;

    public Invoice(String series, String correlative, LocalDate issueDate, String issuerRuc, String acquirerDocument) {
        this.series = series;
        this.correlative = correlative;
        this.issueDate = issueDate;
        this.issuerRuc = issuerRuc;
        this.acquirerDocument = acquirerDocument;
    }

    @Override
    public String getSeries() {
        return series;
    }

    @Override
    public String getCorrelative() {
        return correlative;
    }

    @Override
    public LocalDate getIssueDate() {
        return issueDate;
    }

    @Override
    public String getIssuerRuc() {
        return issuerRuc;
    }

    @Override
    public String getAcquirerDocument() {
        return acquirerDocument;
    }
}
