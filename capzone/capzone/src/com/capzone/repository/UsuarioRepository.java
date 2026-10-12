package com.capzone.repository;

import com.capzone.domain.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {
    private static List<Usuario> usuariosDB = new ArrayList<>();

    static {
        // Usuario administrador o de prueba inicial
        usuariosDB.add(new Usuario(1, "Sergio Castaño", "sergio@capzone.com", "123456", "3000000000", LocalDateTime.now()));
    }

    public void guardar(Usuario usuario) {
        usuariosDB.add(usuario);
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuariosDB.stream()
                .filter(u -> u.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }
}