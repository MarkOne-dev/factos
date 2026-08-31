package pe.factos.billing.domain.model;

import pe.factos.issuer.domain.model.valueobjects.Ruc;

import java.time.LocalDate;
import java.util.List;

public sealed interface Cpe permits Invoice, Bill {
    String getSeries();
    String getCorrelative();
    LocalDate getIssueDate();
    Ruc getIssuerRuc();
    String getAcquirerDocument();
    String getAcquirerName();
    List<Item> getItems();
    CpeTotals getTotals();
}
