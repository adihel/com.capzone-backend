package com.capzone.port.in;

import com.capzone.domain.Usuario;

public interface IUsuarioService {
    Usuario registrarUsuario(String nombre, String correo, String password, String telefono);
    Usuario iniciarSesion(String correo, String password);
}
