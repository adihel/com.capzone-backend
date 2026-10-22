package com.capzone.service;

import com.capzone.domain.MensajeContacto;
import java.util.List;

public interface IMensajeContactoService {
    void enviarMensaje(MensajeContacto mensaje);
    List<MensajeContacto> listarMensajes();
    void marcarComoLeido(int id);
}
