package com.capzone.domain;

public enum EstadoPedido {
    PENDIENTE("Pendiente de pago"),
    CONFIRMADO("Pedido confirmado y en bodega"),
    ENVIADO("En camino al domicilio"),
    ENTREGADO("Entregado al cliente");

    private final String descripcion;

    EstadoPedido(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcion() { return descripcion; }

    /** Texto que se guarda en la columna pedidos.estado_pedido (ej. "Confirmado"). */
    public String getNombreBD() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    /** Convierte el texto de la BD al enum. */
    public static EstadoPedido desdeBD(String valor) {
        return valueOf(valor.trim().toUpperCase());
    }
}
