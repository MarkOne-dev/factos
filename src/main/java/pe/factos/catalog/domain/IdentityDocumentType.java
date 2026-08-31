package pe.factos.catalog.domain;

public enum IdentityDocumentType {
    DNI("1", "Documento Nacional de Identidad"),
    FOREIGN_CARD("4", "Carnet de Extranjería"),
    RUC("6", "Registro Único de Contribuyentes"),
    PASSPORT("7", "Pasaporte");

    private final String code;
    private final String description;

    IdentityDocumentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static IdentityDocumentType findByCode(String code) {
        for (IdentityDocumentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported identity document type code: " + code);
    }
}
