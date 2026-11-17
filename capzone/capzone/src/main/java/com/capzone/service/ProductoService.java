package com.capzone.service;

import com.capzone.domain.Producto;
import com.capzone.exception.RecursoNoEncontradoException;
import com.capzone.port.in.IProductoService;
import com.capzone.port.out.IProductoRepository;
import com.capzone.utils.Validador;

import java.util.List;

public class ProductoService implements IProductoService {
    private final IProductoRepository productoRepository;

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> listarCatalogo() {
        return productoRepository.listarProductos();
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        return productoRepository.buscarPorId(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", idProducto));
    }

    @Override
    public void ajustarInventario(int idProducto, int nuevoStock) {
        Validador.noNegativo(nuevoStock, "El stock");
        Producto producto = buscarPorId(idProducto);
        producto.setInventario(nuevoStock);
        productoRepository.actualizar(producto);
    }
}
