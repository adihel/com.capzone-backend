package com.capzone.utils;

import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Entrega conexiones a MySQL/MariaDB (XAMPP).
 * Lee la configuración de db.properties o, si no existe, de las variables de entorno
 * CAPZONE_DB_URL, CAPZONE_DB_USER y CAPZONE_DB_PASSWORD.
 * Busca db.properties en la carpeta desde donde se ejecuta el programa y también
 * junto al proyecto (la carpeta donde está pom.xml).
 */
public final class ConexionBD {
    private static final String URL_POR_DEFECTO =
            "jdbc:mariadb://localhost:3306/capzone_db";

    private static final List<Path> BUSCADO = new ArrayList<>();
    private static final Properties CONFIG = cargarConfig();

    private ConexionBD() {}

    public static Connection obtener() throws SQLException {
        String url = valor("db.url", "CAPZONE_DB_URL", URL_POR_DEFECTO);
        String user = valor("db.user", "CAPZONE_DB_USER", "root");

        // La contraseña puede estar vacía (XAMPP trae root sin contraseña): se escribe "db.password="
        String pass = CONFIG.getProperty("db.password");
        if (pass == null) pass = System.getenv("CAPZONE_DB_PASSWORD");
        if (pass == null) {
            throw new SQLException("No se encontró la línea db.password en db.properties. "
                    + "Crea db.properties junto al pom.xml (si root no tiene contraseña, deja 'db.password=' vacío). "
                    + "Carpetas donde se buscó: " + BUSCADO);
        }
        return DriverManager.getConnection(url, user, pass.trim());
    }

    private static String valor(String clave, String variableEntorno, String porDefecto) {
        String v = CONFIG.getProperty(clave);
        if (v == null || v.isBlank()) v = System.getenv(variableEntorno);
        return (v == null || v.isBlank()) ? porDefecto : v.trim();
    }

    private static Properties cargarConfig() {
        Properties p = new Properties();

        // 1) Carpeta desde donde se ejecuta el programa (y hasta 3 carpetas más arriba)
        Path dir = Paths.get("").toAbsolutePath();
        for (int i = 0; i < 4 && dir != null; i++, dir = dir.getParent()) {
            if (leer(p, dir.resolve("db.properties"))) return p;
        }

        // 2) Junto al proyecto: el código compilado está en <proyecto>/target/classes
        try {
            Path clases = Paths.get(ConexionBD.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path raiz = clases;
            for (int i = 0; i < 3 && raiz != null; i++, raiz = raiz.getParent()) {
                if (leer(p, raiz.resolve("db.properties"))) return p;
            }
        } catch (URISyntaxException | RuntimeException e) {
            // se ignora: se usarán las variables de entorno
        }
        return p;
    }

    private static boolean leer(Properties p, Path archivo) {
        BUSCADO.add(archivo.getParent());
        if (!Files.isRegularFile(archivo)) return false;
        try (Reader in = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            p.load(in);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}