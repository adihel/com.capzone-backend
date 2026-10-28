package com.capzone.repository;

import com.capzone.domain.MensajeContacto;
import java.util.List;

public interface IMensajeContactoRepository {
    void guardar(MensajeContacto mensaje);
    List<MensajeContacto> listarTodos();
    void marcarComoLeido(int id);
}
