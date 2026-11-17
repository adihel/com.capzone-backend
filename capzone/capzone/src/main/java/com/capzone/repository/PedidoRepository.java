package com.capzone.repository;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.EstadoPedido;
import com.capzone.domain.Pedido;
import com.capzone.domain.Producto;
import com.capzone.domain.Usuario;
import com.capzone.exception.StockInsuficienteException;
import com.capzone.port.out.IPedidoRepository;
import com.capzone.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepository implements IPedidoRepository {

    /** Todo o nada: si algo falla (incluido el stock) se hace ROLLBACK y no se guarda nada. */
    @Override
    public void guardar(Pedido pedido, List<DetallePedido> detalles) {
        try (Connection con = ConexionBD.obtener()) {
            con.setAutoCommit(false);
            try {
                insertarPedido(con, pedido);
                for (DetallePedido d : detalles) {
                    insertarDetalle(con, pedido, d);
                    descontarInventario(con, d);
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el pedido: " + e.getMessage(), e);
        }
    }

    private void insertarPedido(Connection con, Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (id_usuario, fecha_pedido, estado_pedido, total) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getUsuario().getIdUsuario());
            ps.setTimestamp(2, Timestamp.valueOf(pedido.getFechaPedido()));
            ps.setString(3, pedido.getEstadoPedido().getNombreBD());
            ps.setBigDecimal(4, pedido.getTotal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) pedido.setIdPedido(keys.getInt(1));
            }
        }
    }

    private void insertarDetalle(Connection con, Pedido pedido, DetallePedido d) throws SQLException {
        String sql = "INSERT INTO detalle_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, pedido.getIdPedido());
            ps.setInt(2, d.getProducto().getIdProducto());
            ps.setInt(3, d.getCantidad());
            ps.setBigDecimal(4, d.getPrecioUnitario());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) d.setIdDetalle(keys.getInt(1));
            }
        }
    }

    /** El WHERE inventario >= ? evita que el stock quede negativo aunque dos personas compren a la vez. */
    private void descontarInventario(Connection con, DetallePedido d) throws SQLException {
        String sql = "UPDATE productos SET inventario = inventario - ? WHERE id_producto = ? AND inventario >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getProducto().getIdProducto());
            ps.setInt(3, d.getCantidad());
            if (ps.executeUpdate() == 0) {
                throw new StockInsuficienteException("Stock insuficiente para '"
                        + d.getProducto().getNombreProducto() + "'. Alguien más pudo haberlo comprado.");
            }
        }
    }

    @Override
    public List<Pedido> listarPorUsuario(int idUsuario) {
        String sql = """
                SELECT p.id_pedido, p.fecha_pedido, p.estado_pedido, p.total,
                       u.id_usuario, u.nombre_completo, u.correo, u.password, u.telefono, u.fecha_registro
                FROM pedidos p
                INNER JOIN usuarios u ON p.id_usuario = u.id_usuario
                WHERE p.id_usuario = ?
                ORDER BY p.fecha_pedido DESC
                """;
        List<Pedido> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = UsuarioRepository.mapear(rs);
                    lista.add(new Pedido(rs.getInt("id_pedido"), u,
                            rs.getTimestamp("fecha_pedido").toLocalDateTime(),
                            EstadoPedido.desdeBD(rs.getString("estado_pedido")),
                            rs.getBigDecimal("total")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los pedidos: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public List<DetallePedido> listarDetalles(Pedido pedido) {
        String sql = """
                SELECT d.id_detalle, d.cantidad, d.precio_unitario,
                       p.id_producto, p.nombre_producto, p.descripcion, p.precio, p.inventario, p.url_imagen,
                       c.id_categoria, c.nombre_categoria, c.descripcion AS desc_categoria
                FROM detalle_pedidos d
                INNER JOIN productos p ON d.id_producto = p.id_producto
                INNER JOIN categorias c ON p.id_categoria = c.id_categoria
                WHERE d.id_pedido = ?
                ORDER BY d.id_detalle
                """;
        List<DetallePedido> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pedido.getIdPedido());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto prod = ProductoRepository.mapear(rs);
                    lista.add(new DetallePedido(rs.getInt("id_detalle"), pedido, prod,
                            rs.getInt("cantidad"), rs.getBigDecimal("precio_unitario")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar el detalle del pedido: " + e.getMessage(), e);
        }
        return lista;
    }
}
