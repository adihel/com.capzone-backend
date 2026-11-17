package com.capzone.service;

import com.capzone.domain.Usuario;
import com.capzone.exception.CredencialesInvalidasException;
import com.capzone.port.in.IUsuarioService;
import com.capzone.port.out.IUsuarioRepository;
import com.capzone.utils.PasswordHasher;
import com.capzone.utils.Validador;

import java.time.LocalDateTime;

public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrarUsuario(String nombre, String correo, String password, String telefono) {
        Validador.noVacio(nombre, "El nombre");
        Validador.correoValido(correo);
        Validador.passwordValida(password);
        Validador.telefonoValido(telefono);

        if (usuarioRepository.buscarPorCorreo(correo.trim()).isPresent()) {
            throw new IllegalStateException("El correo ya está registrado en el sistema.");
        }

        // El ID lo genera SQL Server (IDENTITY) y el repositorio lo asigna al guardar
        Usuario nuevo = new Usuario(0, nombre.trim(), correo.trim(),
                PasswordHasher.hash(password), telefono.trim(), LocalDateTime.now());
        usuarioRepository.guardar(nuevo);
        return nuevo;
    }

    @Override
    public Usuario iniciarSesion(String correo, String password) {
        if (correo == null || password == null) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
        }
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo.trim())
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos."));

        if (!PasswordHasher.coincide(password, usuario.getPassword())) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos.");
        }
        return usuario;
    }
}
