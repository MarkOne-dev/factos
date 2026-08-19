CREATE TABLE issuers (
    ruc VARCHAR(11) PRIMARY KEY,
    corporate_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    ubigeo VARCHAR(6),
    cert_base64 TEXT,
    cert_password VARCHAR(255),
    cert_valid_from TIMESTAMP,
    cert_valid_to TIMESTAMP
);

CREATE TABLE authorized_series (
    issuer_ruc VARCHAR(11) NOT NULL,
    code VARCHAR(4) NOT NULL,
    PRIMARY KEY (issuer_ruc, code),
    CONSTRAINT fk_series_issuer FOREIGN KEY (issuer_ruc) REFERENCES issuers(ruc) ON DELETE CASCADE
);

CREATE TABLE cpes (
    issuer_ruc VARCHAR(11) NOT NULL,
    series VARCHAR(4) NOT NULL,
    correlative VARCHAR(8) NOT NULL,
    cpe_type VARCHAR(2) NOT NULL,
    issue_date DATE NOT NULL,
    acquirer_document VARCHAR(20),
    acquirer_name VARCHAR(255),
    total_taxable NUMERIC(12, 2) NOT NULL,
    total_exonerated NUMERIC(12, 2) NOT NULL,
    total_inactive NUMERIC(12, 2) NOT NULL,
    total_igv NUMERIC(12, 2) NOT NULL,
    total_free NUMERIC(12, 2) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    state VARCHAR(20) NOT NULL,
    cdr_description TEXT,
    error_code VARCHAR(10),
    error_message TEXT,
    PRIMARY KEY (issuer_ruc, series, correlative),
    CONSTRAINT fk_cpe_issuer FOREIGN KEY (issuer_ruc) REFERENCES issuers(ruc)
);

CREATE TABLE cpe_items (
    id BIGSERIAL PRIMARY KEY,
    issuer_ruc VARCHAR(11) NOT NULL,
    series VARCHAR(4) NOT NULL,
    correlative VARCHAR(8) NOT NULL,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity NUMERIC(12, 4) NOT NULL,
    unit_value NUMERIC(12, 2) NOT NULL,
    unit_price NUMERIC(12, 2) NOT NULL,
    affectation_code VARCHAR(2) NOT NULL,
    taxable_base NUMERIC(12, 2) NOT NULL,
    igv NUMERIC(12, 2) NOT NULL,
    total NUMERIC(12, 2) NOT NULL,
    CONSTRAINT fk_item_cpe FOREIGN KEY (issuer_ruc, series, correlative) REFERENCES cpes(issuer_ruc, series, correlative) ON DELETE CASCADE
);
