package pe.factos.issuer.domain.model.commands;

public record CreateIssuerCommand(
        String ruc,
        String corporateName,
        String address,
        String ubigeo
) {
}
