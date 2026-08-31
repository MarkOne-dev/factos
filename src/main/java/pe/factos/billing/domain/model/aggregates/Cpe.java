package pe.factos.billing.domain.model.aggregates;

import lombok.Getter;
import pe.factos.billing.domain.model.entities.Item;
import pe.factos.billing.domain.model.valueobjects.CpeStatus;
import pe.factos.billing.domain.model.valueobjects.CpeTotals;
import pe.factos.issuer.domain.model.valueobjects.Ruc;
import pe.factos.shared.domain.BusinessException;
import pe.factos.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
public class Cpe extends AbstractDomainAggregateRoot<Cpe> {
    private final String series;
    private final String correlative;
    private final String cpeType; // "01" Factura, "03" Boleta
    private final LocalDate issueDate;
    private final Ruc issuerRuc;
    private final String acquirerDocument;
    private final String acquirerName;
    private final List<Item> items;
    private final CpeTotals totals;
    private CpeStatus status;

    public Cpe(String series, String correlative, String cpeType, LocalDate issueDate, Ruc issuerRuc,
               String acquirerDocument, String acquirerName, List<Item> items, CpeTotals totals, CpeStatus status) {
        if (series == null || correlative == null || issueDate == null || issuerRuc == null) {
            throw new BusinessException("CPE header fields cannot be null");
        }
        if (acquirerDocument == null || acquirerDocument.isBlank()) {
            throw new BusinessException("Acquirer document cannot be empty");
        }
        if (items == null || items.isEmpty()) {
            throw new BusinessException("CPE must have at least one item");
        }
        this.series = series;
        this.correlative = correlative;
        this.cpeType = cpeType;
        this.issueDate = issueDate;
        this.issuerRuc = issuerRuc;
        this.acquirerDocument = acquirerDocument;
        this.acquirerName = acquirerName;
        this.items = List.copyOf(items);
        this.totals = totals != null ? totals : CpeTotals.empty(items.get(0).getTotal().currency());
        this.status = status != null ? status : CpeStatus.EMITTED;
    }

    public Cpe(String series, String correlative, String cpeType, LocalDate issueDate, Ruc issuerRuc,
               String acquirerDocument, String acquirerName, List<Item> items, CpeTotals totals) {
        this(series, correlative, cpeType, issueDate, issuerRuc, acquirerDocument, acquirerName, items, totals, CpeStatus.EMITTED);
    }

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void markAsAccepted() {
        this.status = CpeStatus.ACCEPTED;
    }

    public void markAsRejected() {
        this.status = CpeStatus.REJECTED;
    }

    public void markAsCancelled() {
        this.status = CpeStatus.CANCELLED;
    }
}
