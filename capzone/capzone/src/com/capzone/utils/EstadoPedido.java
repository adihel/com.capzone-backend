package com.capzone.utils;

public enum EstadoPedido {
    PENDIENTE("Pendiente de pago"),
    CONFIRMADO("Pedido confirmado y en bodega"),
    ENVIADO("En camino al domicilio"),
    ENTREGADO("Entregado al cliente");

    private String descripcion;

    EstadoPedido(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
