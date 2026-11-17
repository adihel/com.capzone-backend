package com.capzone.service;

import com.capzone.domain.MensajeContacto;
import com.capzone.port.in.IMensajeContactoService;
import com.capzone.port.out.IMensajeContactoRepository;
import com.capzone.utils.Validador;

import java.util.List;

public class MensajeContactoService implements IMensajeContactoService {
    private final IMensajeContactoRepository repository;

    public MensajeContactoService(IMensajeContactoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void enviarMensaje(String nombre, String correo, String asunto, String texto) {
        Validador.noVacio(nombre, "El nombre");
        Validador.correoValido(correo);
        Validador.noVacio(texto, "El mensaje");
        repository.guardar(new MensajeContacto(nombre.trim(), correo.trim(),
                asunto == null ? "" : asunto.trim(), texto.trim()));
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
