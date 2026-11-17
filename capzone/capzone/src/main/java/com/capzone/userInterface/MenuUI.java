package com.capzone.userInterface;

import com.capzone.domain.Pedido;
import com.capzone.domain.Producto;
import com.capzone.domain.Usuario;
import com.capzone.port.in.IPedidoService;
import com.capzone.port.in.IProductoService;
import com.capzone.port.in.IUsuarioService;
import com.capzone.utils.ConsolaHelper;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuUI {
    private final IProductoService productoService;
    private final IUsuarioService usuarioService;
    private final IPedidoService pedidoService;
    private final ConsolaHelper consola = new ConsolaHelper();

    private Usuario usuarioLogueado = null;

    public MenuUI(IProductoService productoService, IUsuarioService usuarioService, IPedidoService pedidoService) {
        this.productoService = productoService;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            int opcion = consola.leerEntero("Elige una opción: ");
            try {
                salir = (usuarioLogueado == null) ? opcionInvitado(opcion) : opcionCliente(opcion);
            } catch (RuntimeException e) {
                consola.imprimir("⚠️ Error: " + e.getMessage());
            }
        }
        consola.imprimir("\n¡Gracias por visitar CapZone! Hasta pronto.");
    }

    private void mostrarMenu() {
        consola.imprimir("\n=================================");
        consola.imprimir("       🧢 SISTEMA CAPZONE 🧢       ");
        consola.imprimir("=================================");
        if (usuarioLogueado == null) {
            consola.imprimir("1. Ver catálogo de gorras");
            consola.imprimir("2. Iniciar sesión");
            consola.imprimir("3. Registrarse");
            consola.imprimir("4. Salir");
        } else {
            consola.imprimir("👤 Sesión activa: " + usuarioLogueado.getNombreCompleto());
            consola.imprimir("1. Ver catálogo de gorras");
            consola.imprimir("2. Comprar");
            consola.imprimir("3. Mi historial de pedidos");
            consola.imprimir("4. Módulo Admin: ajustar inventario");
            consola.imprimir("5. Cerrar sesión");
        }
    }

    /** @return true si el usuario quiere salir del programa */
    private boolean opcionInvitado(int opcion) {
        switch (opcion) {
            case 1 -> mostrarCatalogo();
            case 2 -> iniciarSesion();
            case 3 -> registrarse();
            case 4 -> { return true; }
            default -> consola.imprimir("⚠️ Opción inválida.");
        }
        return false;
    }

    private boolean opcionCliente(int opcion) {
        switch (opcion) {
            case 1 -> mostrarCatalogo();
            case 2 -> comprar();
            case 3 -> mostrarHistorial();
            case 4 -> ajustarInventario();
            case 5 -> {
                usuarioLogueado = null;
                consola.imprimir("🔒 Sesión cerrada exitosamente.");
            }
            default -> consola.imprimir("⚠️ Opción inválida.");
        }
        return false;
    }

    private void iniciarSesion() {
        consola.imprimir("\n--- INICIAR SESIÓN ---");
        String correo = consola.leerTexto("Correo: ");
        String pass = consola.leerTexto("Contraseña: ");
        usuarioLogueado = usuarioService.iniciarSesion(correo, pass);
        consola.imprimir("✅ ¡Bienvenido de nuevo, " + usuarioLogueado.getNombreCompleto() + "!");
    }

    private void registrarse() {
        consola.imprimir("\n--- REGISTRO DE NUEVO USUARIO ---");
        String nombre = consola.leerTexto("Nombre completo: ");
        String correo = consola.leerTexto("Correo: ");
        String pass = consola.leerTexto("Contraseña (mínimo 6 caracteres): ");
        String telefono = consola.leerTexto("Teléfono: ");
        usuarioLogueado = usuarioService.registrarUsuario(nombre, correo, pass, telefono);
        consola.imprimir("🎉 ¡Registro exitoso! Sesión iniciada automáticamente.");
    }

    private void comprar() {
        consola.imprimir("\n--- PROCESAR NUEVO PEDIDO ---");
        mostrarCatalogo();
        Map<Integer, Integer> items = new LinkedHashMap<>();
        while (true) {
            int id = consola.leerEntero("ID del producto (0 para terminar): ");
            if (id == 0) break;
            int cantidad = consola.leerEntero("Cantidad: ");
            items.merge(id, cantidad, Integer::sum);
        }
        if (items.isEmpty()) {
            consola.imprimir("No agregaste productos.");
            return;
        }
        Pedido pedido = pedidoService.crearPedido(usuarioLogueado, items);
        consola.imprimir("\n🎉 ¡Pedido realizado con éxito!");
        consola.imprimir("Número de pedido: " + pedido.getIdPedido());
        consola.imprimir("Total a pagar: $" + pedido.getTotal());
        consola.imprimir("Estado: " + pedido.getEstadoPedido().getDescripcion());
    }

    private void mostrarHistorial() {
        consola.imprimir("\n--- MIS PEDIDOS ---");
        List<Pedido> pedidos = pedidoService.historialDe(usuarioLogueado);
        if (pedidos.isEmpty()) {
            consola.imprimir("Aún no tienes pedidos.");
            return;
        }
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Pedido p : pedidos) {
            consola.imprimir("Pedido #" + p.getIdPedido() + " | " + p.getFechaPedido().format(fmt)
                    + " | " + p.getEstadoPedido().getDescripcion() + " | Total: $" + p.getTotal());
        }
    }

    private void ajustarInventario() {
        consola.imprimir("\n--- MÓDULO ADMINISTRATIVO: AJUSTAR INVENTARIO ---");
        mostrarCatalogo();
        int id = consola.leerEntero("ID del producto a actualizar: ");
        int nuevoStock = consola.leerEntero("Nuevo stock disponible: ");
        productoService.ajustarInventario(id, nuevoStock);
        consola.imprimir("✅ Inventario actualizado correctamente.");
    }

    private void mostrarCatalogo() {
        consola.imprimir("\n--- CATÁLOGO DISPONIBLE ---");
        for (Producto p : productoService.listarCatalogo()) {
            consola.imprimir("ID: " + p.getIdProducto() + " | " + p.getNombreProducto()
                    + " | Precio: $" + p.getPrecio() + " | Stock: " + p.getInventario());
        }
    }
}
