package com.capzone.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Entrega conexiones a SQL Server.
 * Lee la configuración de db.properties (en la raíz del proyecto) o, si no existe,
 * de las variables de entorno CAPZONE_DB_URL, CAPZONE_DB_USER y CAPZONE_DB_PASSWORD.
 */
public final class ConexionBD {
    private static final String URL_POR_DEFECTO =
            "jdbc:sqlserver://localhost:1433;databaseName=capzone_db;encrypt=true;trustServerCertificate=true";

    private static final Properties CONFIG = cargarConfig();

    private ConexionBD() {}

    public static Connection obtener() throws SQLException {
        String url = valor("db.url", "CAPZONE_DB_URL", URL_POR_DEFECTO);
        String user = valor("db.user", "CAPZONE_DB_USER", "sa");
        String pass = valor("db.password", "CAPZONE_DB_PASSWORD", null);
        if (pass == null) {
            throw new SQLException("Falta la contraseña de la base de datos. "
                    + "Copia db.properties.example como db.properties y escribe tu contraseña.");
        }
        return DriverManager.getConnection(url, user, pass);
    }

    private static String valor(String clave, String variableEntorno, String porDefecto) {
        String v = CONFIG.getProperty(clave);
        if (v == null || v.isBlank()) v = System.getenv(variableEntorno);
        return (v == null || v.isBlank()) ? porDefecto : v.trim();
    }

    private static Properties cargarConfig() {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream("db.properties")) {
            p.load(in);
        } catch (IOException e) {
            // Si no existe el archivo se usan las variables de entorno.
        }
        return p;
    }
}
