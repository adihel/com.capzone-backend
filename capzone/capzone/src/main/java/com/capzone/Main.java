package com.capzone;

import com.capzone.repository.MensajeContactoRepository;
import com.capzone.repository.PedidoRepository;
import com.capzone.repository.ProductoRepository;
import com.capzone.repository.UsuarioRepository;
import com.capzone.service.PedidoService;
import com.capzone.service.ProductoService;
import com.capzone.service.UsuarioService;
import com.capzone.userInterface.MenuUI;
import com.capzone.utils.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // 1) Probar la conexión antes de mostrar el menú
        try (Connection con = ConexionBD.obtener()) {
            System.out.println("✅ Conectado a la base de datos: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("❌ No se pudo conectar a la base de datos: " + e.getMessage());
            System.out.println("Revisa db.properties, que SQL Server esté encendido (TCP/IP, puerto 1433) y que hayas ejecutado database/capzone_db.sql");
            return;
        }

        // 2) Repositorios (acceso a la BD)
        ProductoRepository productoRepo = new ProductoRepository();
        UsuarioRepository usuarioRepo = new UsuarioRepository();
        PedidoRepository pedidoRepo = new PedidoRepository();
        MensajeContactoRepository mensajeRepo = new MensajeContactoRepository(); // listo para usar cuando agregues el módulo de contacto

        // 3) Servicios (lógica de negocio)
        ProductoService productoService = new ProductoService(productoRepo);
        UsuarioService usuarioService = new UsuarioService(usuarioRepo);
        PedidoService pedidoService = new PedidoService(pedidoRepo, productoRepo);

        // 4) Interfaz
        new MenuUI(productoService, usuarioService, pedidoService).iniciar();
    }
}
