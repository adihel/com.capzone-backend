package com.capzone.exception;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String message) {
        super(message);
    }

    public StockInsuficienteException(String producto, int disponible, int solicitado) {
        super("Stock insuficiente para '" + producto + "': disponible " + disponible + ", solicitado " + solicitado + ".");
    }
}
