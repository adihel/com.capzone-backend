package com.capzone.port.in;

import com.capzone.domain.MensajeContacto;

import java.util.List;

public interface IMensajeContactoService {
    void enviarMensaje(String nombre, String correo, String asunto, String texto);
    List<MensajeContacto> listarMensajes();
    void marcarComoLeido(int id);
}
