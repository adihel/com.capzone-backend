package com.capzone.repository;

import com.capzone.domain.Categoria;
import com.capzone.domain.Producto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository implements IProductoRepository {
    private static List<Producto> catalogo = new ArrayList<>();

    static {
        Categoria snapback = new Categoria(1, "Snapback", "Gorras planas urbanas");
        catalogo.add(new Producto(1, snapback, "Gorra NY Yankees Classic", "Original color negro", new BigDecimal("120000"), 10, "ny.jpg"));
        catalogo.add(new Producto(2, snapback, "Gorra LA Dodgers Blue", "Edición especial", new BigDecimal("115000"), 5, "la.jpg"));
    }

    @Override
    public List<Producto> listarProductos() {
        return catalogo;
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        return catalogo.stream().filter(p -> p.getIdProducto() == id).findFirst();
    }

    @Override
    public void actualizar(Producto producto) {
        // Lógica de actualización en la lista en memoria si es requerida
    }
}
