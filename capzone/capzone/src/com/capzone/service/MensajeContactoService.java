package com.capzone.service;

import com.capzone.domain.MensajeContacto;
import com.capzone.repository.IMensajeContactoRepository;
import java.util.List;

public class MensajeContactoService implements IMensajeContactoService {

    private final IMensajeContactoRepository repository;

    public MensajeContactoService(IMensajeContactoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void enviarMensaje(MensajeContacto mensaje) {
        if (mensaje.getNombre() == null || mensaje.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (mensaje.getCorreo() == null || !mensaje.getCorreo().contains("@")) {
            throw new IllegalArgumentException("El correo no es válido");
        }
        if (mensaje.getMensaje() == null || mensaje.getMensaje().isBlank()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        repository.guardar(mensaje);
    }

    @Override
    public List<MensajeContacto> listarMensajes() {
        return repository.listarTodos();
    }

    @Override
    public void marcarComoLeido(int id) {
        repository.marcarComoLeido(id);
    }
}
