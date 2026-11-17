package com.capzone.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Hash SHA-256 para no guardar contraseñas en texto plano.
 * Para producción usa BCrypt (spring-security-crypto o jbcrypt), que además incluye salt.
 */
public final class PasswordHasher {
    private PasswordHasher() {}

    public static String hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(md.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }

    public static boolean coincide(String passwordPlano, String hashGuardado) {
        return hash(passwordPlano).equals(hashGuardado);
    }
}
