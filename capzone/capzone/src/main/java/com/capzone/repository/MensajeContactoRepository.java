package com.capzone.repository;

import com.capzone.domain.MensajeContacto;
import com.capzone.exception.RecursoNoEncontradoException;
import com.capzone.port.out.IMensajeContactoRepository;
import com.capzone.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MensajeContactoRepository implements IMensajeContactoRepository {

    @Override
    public void guardar(MensajeContacto m) {
        String sql = "INSERT INTO mensajes_contacto (nombre, correo, asunto, mensaje, fecha_envio, leido) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getCorreo());
            ps.setString(3, m.getAsunto());
            ps.setString(4, m.getMensaje());
            ps.setTimestamp(5, Timestamp.valueOf(m.getFechaEnvio()));
            ps.setBoolean(6, m.isLeido());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) m.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el mensaje: " + e.getMessage(), e);
        }
    }

    @Override
    public List<MensajeContacto> listarTodos() {
        String sql = "SELECT id_contacto, nombre, correo, asunto, mensaje, fecha_envio, leido "
                + "FROM mensajes_contacto ORDER BY fecha_envio DESC";
        List<MensajeContacto> lista = new ArrayList<>();
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MensajeContacto m = new MensajeContacto(rs.getString("nombre"), rs.getString("correo"),
                        rs.getString("asunto"), rs.getString("mensaje"));
                m.setId(rs.getInt("id_contacto"));
                m.setFechaEnvio(rs.getTimestamp("fecha_envio").toLocalDateTime());
                m.setLeido(rs.getBoolean("leido"));
                lista.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los mensajes: " + e.getMessage(), e);
        }
        return lista;
    }

    @Override
    public void marcarComoLeido(int id) {
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement("UPDATE mensajes_contacto SET leido = 1 WHERE id_contacto = ?")) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new RecursoNoEncontradoException("No existe un mensaje con id " + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al marcar el mensaje: " + e.getMessage(), e);
        }
    }
}
