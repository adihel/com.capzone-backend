package com.capzone.repository;

import com.capzone.domain.MensajeContacto;

import java.util.ArrayList;
import java.util.List;

public class MensajeContactoRepository implements IMensajeContactoRepository {

    private final List<MensajeContacto> mensajes = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public void guardar(MensajeContacto mensaje) {
        mensaje.setId(siguienteId++);
        mensajes.add(mensaje);
    }

    @Override
    public List<MensajeContacto> listarTodos() {
        return new ArrayList<>(mensajes);
    }

    @Override
    public void marcarComoLeido(int id) {
        for (MensajeContacto m : mensajes) {
            if (m.getId() == id) {
                m.setLeido(true);
                return;
            }
        }
        throw new RuntimeException("No existe un mensaje con id " + id);
    }
}