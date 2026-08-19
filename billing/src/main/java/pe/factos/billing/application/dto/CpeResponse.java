package pe.factos.billing.application.dto;

public record CpeResponse(
        String series,
        String correlative,
        boolean accepted,
        String cdrDescription,
        String errorCode,
        String errorMessage
) {}
