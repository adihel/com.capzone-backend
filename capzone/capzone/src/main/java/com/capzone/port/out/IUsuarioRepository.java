package com.capzone.port.out;

import com.capzone.domain.Usuario;

import java.util.Optional;

public interface IUsuarioRepository {
    void guardar(Usuario usuario);
    Optional<Usuario> buscarPorCorreo(String correo);
}
