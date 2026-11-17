USE master;
GO
CREATE LOGIN capzone_app WITH PASSWORD = 'CapZone2026', CHECK_POLICY = OFF;
GO
USE capzone_db;
GO
CREATE USER capzone_app FOR LOGIN capzone_app;
ALTER ROLE db_owner ADD MEMBER capzone_app;
GO