package pe.factos.billing.domain.model.commands;

import java.time.LocalDate;
import java.util.List;

public record EmitCpeCommand(
        String series,
        String correlative,
        String cpeType,
        LocalDate issueDate,
        String issuerRuc,
        String acquirerDocument,
        String acquirerName,
        List<ItemCommand> items,
        String currency
) {
}
