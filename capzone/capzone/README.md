# CapZone

Tienda de gorras: backend en Java (consola) + base de datos SQL Server.

## Estructura
- `src/main/java/com/capzone/` — código Java (domain, port, service, repository, userInterface, utils)
- `database/capzone_db.sql` — script que crea `capzone_db`, tablas, datos de prueba y procedimientos
- `docs/` — diagrama, problema y entidades

## Cómo ejecutarlo
1. En SSMS, abre `database/capzone_db.sql` y ejecútalo completo (F5).
2. Copia `db.properties.example` como `db.properties` y escribe tu contraseña de SQL Server.
3. Abre la carpeta en IntelliJ (detecta `pom.xml` y descarga el driver) y ejecuta `com.capzone.Main`.

Usuarios de prueba (contraseña `123456`): `sergio@capzone.com`, `juan.perez@email.com`.

## Arquitectura
`MenuUI → Service (port.in) → Repository (port.out) → SQL Server (JDBC)`
