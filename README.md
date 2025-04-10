# 📦 franchiseMicroservice

Microservicio desarrollado para la gestión de franquicias, sucursales y productos. Implementado en Java con Spring Boot WebFlux, utilizando programación reactiva y PostgreSQL como base de datos. Incluye una API RESTful documentada mediante OpenAPI y un diseño limpio basado en arquitectura por capas.

---

## 📚 Contenido

- [🔧 Tecnologías](#-tecnologías)
- [🧱 Modelo de Datos](#-modelo-de-datos)
- [📑 Documentación de la API](#-documentación-de-la-api)
- [⚙️ Variables de Entorno](#-variables-de-entorno)
- [🚀 Cómo ejecutar el proyecto](#-cómo-ejecutar-el-proyecto)
- [🧪 Ejecutar Tests](#-ejecutar-tests)
- [📦 Estructura del proyecto](#-estructura-del-proyecto)

---

## 🔧 Tecnologías

- Java 21
- Spring Boot (WebFlux)
- PostgreSQL
- Gradle
- OpenAPI (Swagger)
- Dotenv (para configuración vía `.env`)
- GitLab
- Docker (pendiente de integrar 😅)

---

## 🧱 Modelo de Datos

El sistema maneja las siguientes entidades:

- **Franquicia**: Representa una cadena principal.
- **Sucursal**: Asociada a una franquicia específica.
- **Producto**: Relacionado con una sucursal, con control de stock.

### 🗂️ Diagrama de la Base de Datos

![Diagrama de BD](docs/diagrama-bd.png)

---

## 📑 Documentación de la API

La API está documentada con OpenAPI y se encuentra en el archivo `openApi.yaml`.

🔗 [Ver en Swagger Editor](https://editor.swagger.io/?url=https://gitlab.com/miguel.bernal1/franchisemicroservice/-/raw/main/openApi.yaml)

Incluye:

- Rutas completas para CRUD de franquicia, sucursal y producto
- Ejemplos de peticiones y respuestas
- Validaciones y códigos de estado
- Documentación mantenible y clara

---

## ⚙️ Variables de Entorno

Crea un archivo `.env` en la raíz del proyecto con contenido similar:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=franchise_db
DB_USER=postgres
DB_PASSWORD=tu_clave
```
---

## 🚀 Cómo ejecutar el proyecto

```bash
git clone https://gitlab.com/miguel.bernal1/franchisemicroservice.git
cd franchisemicroservice
./gradlew bootRun
```
---

## 🧪 Ejecutar Tests
```bash
./gradlew test
```
---

## 📦 Estructura del proyecto
```bash
├── src
│   ├── main
│   │   ├── java/com/franquicia/...
│   │   └── resources
│   │       ├── application.properties
│   │       └── ...
│   └── test
├── docs
│   └── diagrama-bd.png
├── openApi.yaml
├── .env
├── build.gradle
└── README.md
```
---