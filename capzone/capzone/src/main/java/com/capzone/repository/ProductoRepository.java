package com.capzone.repository;

import com.capzone.domain.Categoria;
import com.capzone.domain.Producto;
import com.capzone.exception.RecursoNoEncontradoException;
import com.capzone.port.out.IProductoRepository;
import com.capzone.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository implements IProductoRepository {

    static final String SELECT_BASE = """
            SELECT p.id_producto, p.nombre_producto, p.descripcion, p.precio, p.inventario, p.url_imagen,
                   c.id_categoria, c.nombre_categoria, c.descripcion AS desc_categoria
            FROM productos p
            INNER JOIN categorias c ON p.id_categoria = c.id_categoria
            """;

    @Override
    public List<Producto> listarProductos() {
        List<Producto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + " ORDER BY p.id_producto");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar productos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(SELECT_BASE + " WHERE p.id_producto = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el producto: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre_producto = ?, descripcion = ?, precio = ?, "
                + "inventario = ?, url_imagen = ? WHERE id_producto = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getDescripcion());
            ps.setBigDecimal(3, p.getPrecio());
            ps.setInt(4, p.getInventario());
            ps.setString(5, p.getUrlImagen());
            ps.setInt(6, p.getIdProducto());
            if (ps.executeUpdate() == 0) {
                throw new RecursoNoEncontradoException("Producto", p.getIdProducto());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el producto: " + e.getMessage(), e);
        }
    }

    /** Convierte la fila actual (con las columnas de SELECT_BASE) en un Producto. */
    static Producto mapear(ResultSet rs) throws SQLException {
        Categoria cat = new Categoria(rs.getInt("id_categoria"),
                rs.getString("nombre_categoria"), rs.getString("desc_categoria"));
        return new Producto(rs.getInt("id_producto"), cat, rs.getString("nombre_producto"),
                rs.getString("descripcion"), rs.getBigDecimal("precio"),
                rs.getInt("inventario"), rs.getString("url_imagen"));
    }
}
