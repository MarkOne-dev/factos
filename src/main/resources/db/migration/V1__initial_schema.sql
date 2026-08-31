-- Flyway Initial Database Migration for Factos Bounded Contexts

-- 1. Catalog Bounded Context
CREATE TABLE IF NOT EXISTS catalog_items (
    id UUID PRIMARY KEY,
    catalog_code VARCHAR(10) NOT NULL,
    item_code VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_catalog_item_code UNIQUE (catalog_code, item_code)
);

CREATE INDEX IF NOT EXISTS idx_catalog_code ON catalog_items(catalog_code);

-- 2. Issuer Bounded Context
CREATE TABLE IF NOT EXISTS issuers (
    id UUID PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    corporate_name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    ubigeo VARCHAR(6),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_issuer_ruc ON issuers(ruc);

-- 3. Billing Bounded Context
CREATE TABLE IF NOT EXISTS comprobantes (
    id UUID PRIMARY KEY,
    series VARCHAR(4) NOT NULL,
    correlative VARCHAR(8) NOT NULL,
    cpe_type VARCHAR(2) NOT NULL,
    issue_date DATE NOT NULL,
    issuer_ruc VARCHAR(11) NOT NULL,
    acquirer_document VARCHAR(15) NOT NULL,
    acquirer_name VARCHAR(255) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'PEN',
    status VARCHAR(20) NOT NULL DEFAULT 'EMITTED',
    total_taxable NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    total_igv NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    total_amount NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT uk_series_correlative UNIQUE (series, correlative)
);

CREATE INDEX IF NOT EXISTS idx_comprobante_series_correlative ON comprobantes(series, correlative);
CREATE INDEX IF NOT EXISTS idx_comprobante_issuer_ruc ON comprobantes(issuer_ruc);

CREATE TABLE IF NOT EXISTS cpe_items (
    id UUID PRIMARY KEY,
    cpe_id UUID REFERENCES comprobantes(id) ON DELETE CASCADE,
    code VARCHAR(30) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity NUMERIC(12, 4) NOT NULL,
    unit_value NUMERIC(12, 4) NOT NULL,
    unit_price NUMERIC(12, 4) NOT NULL,
    affectation_type VARCHAR(30) NOT NULL,
    taxable_base NUMERIC(12, 2) NOT NULL,
    igv NUMERIC(12, 2) NOT NULL,
    total NUMERIC(12, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 4. Security Bounded Context
CREATE TABLE IF NOT EXISTS api_keys (
    id UUID PRIMARY KEY,
    key_value VARCHAR(64) NOT NULL UNIQUE,
    client_name VARCHAR(255) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_api_key_value ON api_keys(key_value);
