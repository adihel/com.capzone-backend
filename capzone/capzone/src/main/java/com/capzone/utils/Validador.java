package com.capzone.utils;

import java.util.regex.Pattern;

public final class Validador {
    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern TELEFONO = Pattern.compile("^\\d{7,15}$");

    private Validador() {}

    public static void noVacio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
    }

    public static void correoValido(String correo) {
        noVacio(correo, "El correo");
        if (!CORREO.matcher(correo.trim()).matches()) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
    }

    public static void telefonoValido(String telefono) {
        noVacio(telefono, "El teléfono");
        if (!TELEFONO.matcher(telefono.trim()).matches()) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 15 dígitos.");
        }
    }

    public static void passwordValida(String password) {
        noVacio(password, "La contraseña");
        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
    }

    public static void positivo(int valor, String campo) {
        if (valor <= 0) {
            throw new IllegalArgumentException(campo + " debe ser mayor que cero.");
        }
    }

    public static void noNegativo(int valor, String campo) {
        if (valor < 0) {
            throw new IllegalArgumentException(campo + " no puede ser negativo.");
        }
    }
}
