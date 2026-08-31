# Guía de Uso e Integración API REST - Servicio Factos

Documentación oficial para la integración del servicio de Facturación Electrónica **`factos`** con sistemas ERP, e-commerce y aplicaciones cliente.

---

## 🔒 1. Autenticación y Seguridad

Todas las peticiones a la API REST (excepto los endpoints de documentación OpenAPI y creación inicial de API Key) requieren la cabecera HTTP **`X-API-KEY`**.

```http
X-API-KEY: tu_api_key_aqui
```

---

## 🛠️ 2. Flujo Completo de Integración con Ejemplos

### Paso 1: Generar una API Key de Acceso
Crea una clave de acceso API para tu sistema cliente o ERP.

* **Endpoint:** `POST /api/v1/api-keys`
* **Content-Type:** `application/json`

#### Ejemplo de Entrada (Request Body):
```json
{
  "clientName": "Sistema ERP Producción",
  "validDays": 365
}
```

#### Ejemplo de Salida (Response - HTTP 201 Created):
```json
{
  "keyValue": "c146989d3831459c95ce9e00d1a202af",
  "clientName": "Sistema ERP Producción",
  "expiresAt": "2027-08-31T20:19:59Z",
  "active": true
}
```

---

### Paso 2: Registrar la Empresa Emisora
Registra los datos de la empresa emisora que emitirá los comprobantes.

* **Endpoint:** `POST /api/v1/issuers`
* **Headers:** `X-API-KEY: c146989d3831459c95ce9e00d1a202af`

#### Ejemplo de Entrada (Request Body):
```json
{
  "ruc": "20123456789",
  "corporateName": "FACTOS TECH PERU S.A.C.",
  "address": "Av. Javier Prado Este 456, San Isidro, Lima",
  "ubigeo": "150131"
}
```

#### Ejemplo de Salida (Response - HTTP 200 OK):
```json
{
  "ruc": "20123456789",
  "corporateName": "FACTOS TECH PERU S.A.C.",
  "address": "Av. Javier Prado Este 456, San Isidro, Lima",
  "ubigeo": "150131"
}
```

---

### Paso 3: Emitir una Factura Electrónica
Emite una nueva Factura Electrónica. El servicio autocalcula la base imponible, IGV (18%), importe total y almacena automáticamente el PDF impreso en el bucket de **Cloudflare R2**.

* **Endpoint:** `POST /api/v1/comprobantes`
* **Headers:** `X-API-KEY: c146989d3831459c95ce9e00d1a202af`

#### Ejemplo de Entrada (Request Body):
```json
{
  "series": "F002",
  "correlative": "00000001",
  "cpeType": "01",
  "issueDate": "2026-08-31",
  "issuerRuc": "20123456789",
  "acquirerDocument": "20555444333",
  "acquirerName": "EMPRESA NUEVA DE PRUEBA S.A.C.",
  "currency": "PEN",
  "items": [
    {
      "code": "SERV-002",
      "description": "Desarrollo de Aplicación Móvil React Native",
      "quantity": 1,
      "unitPrice": 2360.00,
      "affectationType": "TAXABLE_ONEROUS"
    }
  ]
}
```

#### Ejemplo de Salida (Response - HTTP 201 Created):
```json
{
  "series": "F002",
  "correlative": "00000001",
  "cpeType": "01",
  "issueDate": "2026-08-31",
  "issuerRuc": "20123456789",
  "acquirerDocument": "20555444333",
  "acquirerName": "EMPRESA NUEVA DE PRUEBA S.A.C.",
  "status": "EMITTED",
  "totalTaxable": 2000.00,
  "totalIgv": 360.00,
  "totalAmount": 2360.00,
  "currency": "PEN",
  "pdfUrl": "/api/v1/rendering/pdf/F002/00000001",
  "items": [
    {
      "code": "SERV-002",
      "description": "Desarrollo de Aplicación Móvil React Native",
      "quantity": 1.0000,
      "unitPrice": 2360.0000,
      "affectationType": "TAXABLE_ONEROUS"
    }
  ]
}
```

---

### Paso 4: Consultar una Factura por Serie y Correlativo
Obtén los datos de una factura emitida anteriormente.

* **Endpoint:** `GET /api/v1/comprobantes/F002/00000001`
* **Headers:** `X-API-KEY: c146989d3831459c95ce9e00d1a202af`

#### Ejemplo de Salida (Response - HTTP 200 OK):
```json
{
  "series": "F002",
  "correlative": "00000001",
  "cpeType": "01",
  "issueDate": "2026-08-31",
  "issuerRuc": "20123456789",
  "acquirerDocument": "20555444333",
  "acquirerName": "EMPRESA NUEVA DE PRUEBA S.A.C.",
  "status": "EMITTED",
  "totalTaxable": 2000.00,
  "totalIgv": 360.00,
  "totalAmount": 2360.00,
  "currency": "PEN",
  "pdfUrl": "/api/v1/rendering/pdf/F002/00000001",
  "items": [
    {
      "code": "SERV-002",
      "description": "Desarrollo de Aplicación Móvil React Native",
      "quantity": 1.0000,
      "unitPrice": 2360.0000,
      "affectationType": "TAXABLE_ONEROUS"
    }
  ]
}
```

---

### Paso 5: Descargar / Visualizar el PDF Impreso de la Factura
Descarga el documento PDF impreso oficial con el Código QR de la SUNAT integrado.

* **Endpoint:** `GET /api/v1/rendering/pdf/F002/00000001`
* **Headers:** `X-API-KEY: c146989d3831459c95ce9e00d1a202af`

#### Ejemplo de Salida (Response - HTTP 200 OK):
* **Headers de respuesta:**
  * `Content-Type: application/pdf`
  * `Content-Disposition: inline; filename=F002-00000001.pdf`
* **Body:** Archivo binario `.pdf` (Peso aproximado: ~2.95 KB).

---

### Paso 6: Generar Imagen PNG de Código QR SUNAT
Genera una imagen del código QR SUNAT a partir del texto de metadatos.

* **Endpoint:** `GET /api/v1/rendering/qr?content=20123456789|01|F002|00000001|360.00|2360.00|2026-08-31|6|20555444333|MOCK_SIGNATURE`
* **Headers:** `X-API-KEY: c146989d3831459c95ce9e00d1a202af`

#### Ejemplo de Salida (Response - HTTP 200 OK):
* **Headers de respuesta:** `Content-Type: image/png`
* **Body:** Imagen binaria PNG del código QR.

---

## ☁️ 3. Almacenamiento en Cloudflare R2

Cada comprobante PDF generado es persistido automáticamente en el bucket de **Cloudflare R2**:
* **Bucket:** `factos-bucket`
* **Clave de Archivo:** `facturas/{series}-{correlative}.pdf`

Al solicitar la descarga del PDF mediante `/api/v1/rendering/pdf/{series}/{correlative}`, el servicio verifica primero si el archivo existe en Cloudflare R2 para servirlo directamente desde la nube, evitando trabajo de renderizado innecesario.

---

## 🌐 4. Entorno de Pruebas e Interfaz Swagger UI

La documentación interactiva y consola de pruebas en vivo se encuentra disponible en:
* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **OpenAPI Docs JSON:** `http://localhost:8080/v3/api-docs`
