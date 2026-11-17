package com.capzone.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }

    public RecursoNoEncontradoException(String recurso, int id) {
        super(recurso + " con ID " + id + " no encontrado.");
    }
}
