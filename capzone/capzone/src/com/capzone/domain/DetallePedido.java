package com.capzone.domain;

import java.math.BigDecimal;

public class DetallePedido {
    private int idDetalle;
    private Pedido pedido;
    private Producto producto;
    private int cantidad;
    private BigDecimal precioUnitario;

    public DetallePedido(int idDetalle, Pedido pedido, Producto producto, int cantidad, BigDecimal precioUnitario) {
        this.idDetalle = idDetalle;
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Método lógico para calcular subtotal de la línea de pedido
    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    // Getters y Setters
    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}