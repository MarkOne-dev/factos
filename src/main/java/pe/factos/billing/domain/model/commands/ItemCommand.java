package pe.factos.billing.domain.model.commands;

import java.math.BigDecimal;

public record ItemCommand(
        String code,
        String description,
        BigDecimal quantity,
        BigDecimal unitPrice,
        String affectationType
) {
}
