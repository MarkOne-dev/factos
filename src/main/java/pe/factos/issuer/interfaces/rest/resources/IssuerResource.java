package pe.factos.issuer.interfaces.rest.resources;

public record IssuerResource(
        String ruc,
        String corporateName,
        String address,
        String ubigeo
) {
}
