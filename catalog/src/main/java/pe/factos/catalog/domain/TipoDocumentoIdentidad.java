package pe.factos.catalog.domain;

public enum TipoDocumentoIdentidad {
    DNI("1", "Documento Nacional de Identidad"),
    EXTRANJERIA("4", "Carnet de Extranjería"),
    RUC("6", "Registro Único de Contribuyentes"),
    PASAPORTE("7", "Pasaporte");

    private final String codigo;
    private final String descripcion;

    TipoDocumentoIdentidad(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static TipoDocumentoIdentidad buscarPorCodigo(String codigo) {
        for (TipoDocumentoIdentidad tipo : values()) {
            if (tipo.codigo.equals(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de documento de identidad no soportado: " + codigo);
    }
}
