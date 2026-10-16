package com.capzone.service;

import com.capzone.domain.Usuario;
import com.capzone.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class UsuarioService {
    private UsuarioRepository usuarioRepository = new UsuarioRepository();

    public Usuario registrarUsuario(int id, String nombre, String correo, String password, String telefono) {
        Optional<Usuario> existente = usuarioRepository.buscarPorCorreo(correo);
        if (existente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado en el sistema.");
        }

        Usuario nuevoUsuario = new Usuario(id, nombre, correo, password, telefono, LocalDateTime.now());
        usuarioRepository.guardar(nuevoUsuario);
        return nuevoUsuario;
    }

    public Usuario iniciarSesion(String correo, String password) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado."));

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        return usuario;
    }
}