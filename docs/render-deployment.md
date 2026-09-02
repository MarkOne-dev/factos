# Guía de Despliegue en Producción - Render.com

Paso a paso para desplegar el servicio de Facturación Electrónica **`factos`** en **Render.com** utilizando **Docker (Java 26)**, **Aiven Cloud PostgreSQL** y **Cloudflare R2**.

---

## 📋 Requisitos Previos

1. Código fuente en GitHub (`MarkOne-dev/factos`).
2. Cuenta activa en [Render.com](https://render.com/).
3. Credenciales de la Base de Datos **Aiven Cloud PostgreSQL**.
4. Credenciales del Bucket **Cloudflare R2**.

---

## 🚀 Paso a Paso para Desplegar en Render

### Paso 1: Crear un Nuevo Web Service en Render
1. Entra a tu Dashboard en [Render.com](https://dashboard.render.com/).
2. Haz clic en **New +** -> **Web Service**.
3. Selecciona la opción **Build and deploy from a Git repository** y conecta tu repositorio de GitHub (`MarkOne-dev/factos`).

---

### Paso 2: Configuración del Servicio

| Campo | Valor |
| :--- | :--- |
| **Name** | `factos-api` |
| **Region** | `Oregon (US West)` o la más cercana |
| **Branch** | `main` (o `develop`) |
| **Runtime** | **Docker** |
| **Dockerfile Path** | `./Dockerfile` |
| **Instance Type** | `Free` (o `Starter`) |

---

### Paso 3: Configurar las Variables de Entorno (Environment Variables)

En la sección **Environment Variables** en Render, agrega las siguientes claves con los valores de tus servicios en la nube:

| Nombre de Variable | Valor de Ejemplo | Descripción |
| :--- | :--- | :--- |
| `PORT` | `8080` | Puerto asignado dinámicamente por Render |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://shiftiq-db-shiftiq.i.aivencloud.com:19897/defaultdb?sslmode=require` | URL JDBC de Aiven Cloud PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `avnadmin` | Usuario de Aiven Cloud PostgreSQL |
| `SPRING_DATASOURCE_PASSWORD` | `tu_contraseña_aiven` | Contraseña de Aiven Cloud PostgreSQL |
| `CLOUDFLARE_R2_ENDPOINT` | `https://8d009c5a27b703c2906fd1b00b96908c.r2.cloudflarestorage.com` | Endpoint de Cloudflare R2 |
| `CLOUDFLARE_R2_ACCESS_KEY` | `f84173333e47e104c44c7b0881e1cbc5` | Access Key ID de Cloudflare R2 |
| `CLOUDFLARE_R2_SECRET_KEY` | `3ca7f93131e22ccfb157b65af299a16b8f169e93a86e70b02e9d2db3229709d6` | Secret Access Key de Cloudflare R2 |
| `CLOUDFLARE_R2_BUCKET_NAME` | `factos-bucket` | Nombre del Bucket en Cloudflare R2 |

---

### Paso 4: Desplegar y Validar
1. Haz clic en **Create Web Service**.
2. Render comenzará la construcción automáticamente leyendo el `Dockerfile` (Multi-stage build).
3. Una vez finalizado el despliegue, Render te entregará tu URL pública de producción (ej. `https://factos-api.onrender.com`).
4. Abre la consola de Swagger en tu navegador:
   ```text
   https://factos-api.onrender.com/swagger-ui.html
   ```

---

## 🔄 Despliegues Automáticos (CI/CD)

Cada vez que realices un `git push` a la rama `main` en GitHub, Render detectará los cambios, compilará la nueva imagen Docker y desplegará la versión actualizada automáticamente **sin tiempo de caída (Zero-Downtime Deployment)**.

---

## 📖 5. Guía Completa de Uso de la API REST

Una vez desplegado en Render, consulta la guía de integración completa en [`docs/api-guide.md`](api-guide.md) para realizar la generación de API Keys, registro de emisor, emisión de comprobantes y descarga de PDFs.

