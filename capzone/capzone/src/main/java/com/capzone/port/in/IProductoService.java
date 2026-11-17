package com.capzone.port.in;

import com.capzone.domain.Producto;

import java.util.List;

public interface IProductoService {
    List<Producto> listarCatalogo();
    Producto buscarPorId(int idProducto);
    void ajustarInventario(int idProducto, int nuevoStock);
}
