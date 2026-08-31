package pe.factos.billing.application.dto;

import java.time.LocalDate;
import java.util.List;

public record EmitCpeCommand(
        String issuerRuc,
        String series,
        String cpeType,
        String acquirerDocument,
        String acquirerName,
        LocalDate issueDate,
        List<ItemCommand> items
) {}
