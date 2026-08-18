package pe.factos.billing.domain.model;

import java.time.LocalDate;

public sealed interface Cpe permits Invoice, Bill {
    String getSeries();
    String getCorrelative();
    LocalDate getIssueDate();
    String getIssuerRuc();
    String getAcquirerDocument();
}
