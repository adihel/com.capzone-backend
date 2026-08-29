package com.capzone;

public class Pedido {
    private int idPedido;

    public Pedido(int idPedido, Usuario usuario, LocalDateTime fechaPedido, String estadoPedido, BigDecimal total) {
        this.idPedido = idPedido;
        this.usuario = usuario;
        this.fechaPedido = fechaPedido;
        this.estadoPedido = estadoPedido;
        this.total = total;
    }

    private Usuario usuario;
    private LocalDateTime fechaPedido;
    private String estadoPedido;
    private BigDecimal total;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Pedido() {
    }
}
