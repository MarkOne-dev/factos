-- Flyway Seed Data Migration for SUNAT Official Catalogs

-- 1. Catalog 01: Document Types (Tipos de Comprobantes)
INSERT INTO catalog_items (id, catalog_code, item_code, description, active, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'CAT-01', '01', 'Factura Electrónica', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-01', '03', 'Boleta de Venta Electrónica', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-01', '07', 'Nota de Crédito Electrónica', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-01', '08', 'Nota de Débito Electrónica', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 2. Catalog 02: Currency Types (Tipos de Moneda)
INSERT INTO catalog_items (id, catalog_code, item_code, description, active, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'CAT-02', 'PEN', 'Soles', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-02', 'USD', 'Dólares Americanos', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-02', 'EUR', 'Euros', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 3. Catalog 06: Identity Document Types (Tipos de Documento de Identidad)
INSERT INTO catalog_items (id, catalog_code, item_code, description, active, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'CAT-06', '0', 'DOC.TRIB.NO.DOM.SIN.RUC', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-06', '1', 'DNI - Doc. Nacional de Identidad', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-06', '4', 'Carnet de Extranjería', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-06', '6', 'RUC - Reg. Único de Contribuyentes', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-06', '7', 'Pasaporte', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 4. Catalog 07: IGV Affectation Types (Tipos de Afectación al IGV)
INSERT INTO catalog_items (id, catalog_code, item_code, description, active, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'CAT-07', '10', 'Gravado - Operación Onerosa', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-07', '20', 'Exonerado - Operación Onerosa', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-07', '30', 'Inafecto - Operación Onerosa', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;

-- 5. Catalog 51: Operation Types (Tipos de Operación)
INSERT INTO catalog_items (id, catalog_code, item_code, description, active, created_at, updated_at)
VALUES 
    (gen_random_uuid(), 'CAT-51', '0101', 'Venta Interna', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (gen_random_uuid(), 'CAT-51', '0200', 'Exportación', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT DO NOTHING;
