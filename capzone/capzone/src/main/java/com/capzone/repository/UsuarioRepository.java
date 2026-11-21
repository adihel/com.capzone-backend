package com.capzone.repository;

import com.capzone.domain.Usuario;
import com.capzone.port.out.IUsuarioRepository;
import com.capzone.utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class UsuarioRepository implements IUsuarioRepository {

    @Override
    public void guardar(Usuario u) {
        String sql = "INSERT INTO usuarios (nombre_completo, correo, password, telefono) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getPassword()); // ya viene hasheada desde el servicio
            ps.setString(4, u.getTelefono());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setIdUsuario(keys.getInt(1));
            }
        } catch (SQLException e) {
            // 2627 / 2601 = violación de UNIQUE (correo repetido)
            if (e.getErrorCode() == 1062) {
                throw new IllegalStateException("El correo ya está registrado en el sistema.");
            }
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = "SELECT id_usuario, nombre_completo, correo, password, telefono, fecha_registro "
                + "FROM usuarios WHERE correo = ?";
        try (Connection con = ConexionBD.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el usuario: " + e.getMessage(), e);
        }
    }

    /** Convierte la fila actual (columnas de la tabla usuarios) en un Usuario. */
    static Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre_completo"),
                rs.getString("correo"), rs.getString("password"), rs.getString("telefono"),
                rs.getTimestamp("fecha_registro").toLocalDateTime());
    }
}
