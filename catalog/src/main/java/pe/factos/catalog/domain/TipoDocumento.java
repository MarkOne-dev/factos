package pe.factos.catalog.domain;

public enum TipoDocumento {
    FACTURA("01", "Factura"),
    BOLETA("03", "Boleta de Venta");

    private final String codigo;
    private final String descripcion;

    TipoDocumento(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoDocumento buscarPorCodigo(String codigo) {
        for (TipoDocumento tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de documento no soportado: " + codigo);
    }
}
