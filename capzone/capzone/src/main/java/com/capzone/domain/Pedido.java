package com.capzone.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido {
    private int idPedido;
    private Usuario usuario;
    private LocalDateTime fechaPedido;
    private EstadoPedido estadoPedido;
    private BigDecimal total;

    public Pedido(int idPedido, Usuario usuario, LocalDateTime fechaPedido, EstadoPedido estadoPedido, BigDecimal total) {
        this.idPedido = idPedido;
        this.usuario = usuario;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.total = total;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }
    public EstadoPedido getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(EstadoPedido estadoPedido) { this.estadoPedido = estadoPedido; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
