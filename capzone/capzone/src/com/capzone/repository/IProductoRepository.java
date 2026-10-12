package com.capzone.repository;

import com.capzone.domain.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoRepository {
    List<Producto> listarProductos();
    Optional<Producto> buscarPorId(int id);
    void actualizar(Producto producto);
}