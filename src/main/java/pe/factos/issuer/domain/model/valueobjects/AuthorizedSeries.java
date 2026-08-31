package pe.factos.issuer.domain.model.valueobjects;

import pe.factos.shared.domain.BusinessException;

public record AuthorizedSeries(String code, String cpeType) {
    public AuthorizedSeries {
        if (code == null || code.isBlank()) {
            throw new BusinessException("Series code cannot be empty");
        }
        if (cpeType == null || cpeType.isBlank()) {
            throw new BusinessException("CPE type cannot be empty");
        }
    }
}
