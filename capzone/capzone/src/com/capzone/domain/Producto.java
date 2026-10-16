package com.capzone.domain;

import java.math.BigDecimal;

public class Producto {
    private int idProducto;
    private Categoria categoria;
    private String nombreProducto;
    private String descripcion;
    private BigDecimal precio;
    private int inventario;
    private String urlImagen;

    public Producto(int idProducto, Categoria categoria, String nombreProducto, String descripcion, BigDecimal precio, int inventario, String urlImagen) {
        this.idProducto = idProducto;
        this.categoria = categoria;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.inventario = inventario;
        this.urlImagen = urlImagen;
    }

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getInventario() { return inventario; }
    public void setInventario(int inventario) { this.inventario = inventario; }
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}