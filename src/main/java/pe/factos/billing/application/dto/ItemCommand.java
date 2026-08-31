package pe.factos.billing.application.dto;

import java.math.BigDecimal;

public record ItemCommand(
        String code,
        String description,
        BigDecimal quantity,
        BigDecimal unitValue,
        String affectationCode
) {}
