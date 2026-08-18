package pe.factos.catalog.domain;

public enum CpeType {
    INVOICE("01", "Factura"),
    BILL("03", "Boleta de Venta");

    private final String code;
    private final String description;

    CpeType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CpeType findByCode(String code) {
        for (CpeType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported CPE type code: " + code);
    }
}
