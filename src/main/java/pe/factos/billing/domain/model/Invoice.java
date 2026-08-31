package pe.factos.billing.domain.model;

import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.shared.domain.BusinessException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public final class Invoice implements Cpe {
    private final String series;
    private final String correlative;
    private final LocalDate issueDate;
    private final Ruc issuerRuc;
    private final String acquirerDocument;
    private final String acquirerName;
    private final List<Item> items;
    private final CpeTotals totals;

    public Invoice(String series, String correlative, LocalDate issueDate, Ruc issuerRuc,
                   String acquirerDocument, String acquirerName, List<Item> items, CpeTotals totals) {
        if (series == null || correlative == null || issueDate == null || issuerRuc == null) {
            throw new BusinessException("Invoice header fields cannot be null");
        }
        if (acquirerDocument == null || acquirerDocument.isBlank()) {
            throw new BusinessException("Acquirer document cannot be empty");
        }
        if (acquirerName == null || acquirerName.isBlank()) {
            throw new BusinessException("Acquirer name cannot be empty");
        }
        if (!acquirerDocument.matches("^\\d{11}$")) {
            throw new BusinessException("Acquirer document for an Invoice must be an 11-digit number (RUC)");
        }
        if (items == null || items.isEmpty()) {
            throw new BusinessException("Invoice must have at least one item");
        }
        this.series = series;
        this.correlative = correlative;
        this.issueDate = issueDate;
        this.issuerRuc = issuerRuc;
        this.acquirerDocument = acquirerDocument;
        this.acquirerName = acquirerName;
        this.items = List.copyOf(items);
        this.totals = totals != null ? totals : CpeTotals.empty(items.get(0).getTotal().currency());
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
    public Ruc getIssuerRuc() {
        return issuerRuc;
    }

    @Override
    public String getAcquirerDocument() {
        return acquirerDocument;
    }

    @Override
    public String getAcquirerName() {
        return acquirerName;
    }

    @Override
    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    @Override
    public CpeTotals getTotals() {
        return totals;
    }
}
