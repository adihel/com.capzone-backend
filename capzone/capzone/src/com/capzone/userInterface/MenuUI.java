package com.capzone.userinterface;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.Pedido;
import com.capzone.domain.Producto;
import com.capzone.domain.Usuario;
import com.capzone.repository.ProductoRepository;
import com.capzone.service.PedidoService;
import com.capzone.service.UsuarioService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MenuUI {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductoRepository productoRepo = new ProductoRepository();
        PedidoService pedidoService = new PedidoService();
        UsuarioService usuarioService = new UsuarioService();

        Usuario usuarioLogueado = null;
        int opcion = 0;

        do {
            System.out.println("\n=================================");
            System.out.println("       🧢 SISTEMA CAPZONE 🧢       ");
            System.out.println("=================================");

            if (usuarioLogueado == null) {
                System.out.println("1. Ver catálogo de gorras");
                System.out.println("2. Iniciar Sesión");
                System.out.println("3. Registrarse");
                System.out.println("4. Salir");
            } else {
                System.out.println("👤 Sesión activa: " + usuarioLogueado.getNombreCompleto());
                System.out.println("1. Ver catálogo de gorras");
                System.out.println("2. Simular compra de producto");
                System.out.println("3. Módulo Admin: Ajustar Inventario");
                System.out.println("4. Cerrar Sesión");
            }

            System.out.print("Elige una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
            } else {
                System.out.println("❌ Por favor, ingresa un número válido.");
                scanner.next();
                continue;
            }

            if (usuarioLogueado == null) {
                // Menú para usuarios no autenticados
                switch (opcion) {
                    case 1:
                        mostrarCatalogo(productoRepo);
                        break;
                    case 2:
                        System.out.println("\n--- INICIAR SESIÓN ---");
                        System.out.print("Correo: ");
                        String correoLogin = scanner.next();
                        System.out.print("Contraseña: ");
                        String passLogin = scanner.next();
                        try {
                            usuarioLogueado = usuarioService.iniciarSesion(correoLogin, passLogin);
                            System.out.println("✅ ¡Bienvenido de nuevo, " + usuarioLogueado.getNombreCompleto() + "!");
                        } catch (RuntimeException e) {
                            System.out.println("⚠️ Error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("\n--- REGISTRO DE NUEVO USUARIO ---");
                        System.out.print("Nombre completo: ");
                        scanner.nextLine(); // limpiar buffer
                        String nombre = scanner.nextLine();
                        System.out.print("Correo: ");
                        String correo = scanner.next();
                        System.out.print("Contraseña: ");
                        String password = scanner.next();
                        System.out.print("Teléfono: ");
                        String telefono = scanner.next();

                        try {
                            int nuevoId = (int)(Math.random() * 1000);
                            usuarioLogueado = usuarioService.registrarUsuario(nuevoId, nombre, correo, password, telefono);
                            System.out.println("🎉 ¡Registro exitoso! Sesión iniciada automáticamente.");
                        } catch (RuntimeException e) {
                            System.out.println("⚠️ Error al registrarse: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.println("\n¡Gracias por visitar CapZone! Hasta pronto.");
                        break;
                    default:
                        System.out.println("⚠️ Opción inválida.");
                }
            } else {
                // Menú para usuarios autenticados
                switch (opcion) {
                    case 1:
                        mostrarCatalogo(productoRepo);
                        break;
                    case 2:
                        System.out.println("\n--- PROCESAR NUEVO PEDIDO ---");
                        List<Producto> disponibles = productoRepo.listarProductos();
                        mostrarCatalogo(productoRepo);

                        System.out.print("Ingresa el ID del producto que deseas comprar: ");
                        int idProd = scanner.nextInt();

                        Producto productoSeleccionado = disponibles.stream()
                                .filter(p -> p.getIdProducto() == idProd)
                                .findFirst()
                                .orElse(null);

                        if (productoSeleccionado == null) {
                            System.out.println("❌ Producto no encontrado.");
                            break;
                        }

                        System.out.print("Ingresa la cantidad que deseas comprar: ");
                        int cantidad = scanner.nextInt();

                        try {
                            Pedido nuevoPedido = new Pedido(100 + (int)(Math.random() * 900), usuarioLogueado, LocalDateTime.now(), "PENDIENTE", BigDecimal.ZERO);
                            DetallePedido detalle = new DetallePedido(1, nuevoPedido, productoSeleccionado, cantidad, productoSeleccionado.getPrecio());

                            pedidoService.procesarPedido(nuevoPedido, Collections.singletonList(detalle));

                            System.out.println("\n🎉 ¡Pedido realizado con éxito!");
                            System.out.println("Número de Pedido: " + nuevoPedido.getIdPedido());
                            System.out.println("Total a pagar: $" + nuevoPedido.getTotal());
                            System.out.println("Inventario restante de " + productoSeleccionado.getNombreProducto() + ": " + productoSeleccionado.getInventario());

                        } catch (RuntimeException e) {
                            System.out.println("⚠️ Error al procesar la compra: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("\n--- MÓDULO ADMINISTRATIVO: AJUSTAR INVENTARIO ---");
                        mostrarCatalogo(productoRepo);
                        System.out.print("Ingresa el ID del producto a actualizar: ");
                        int idUpdate = scanner.nextInt();

                        Producto prodUpdate = productoRepo.listarProductos().stream()
                                .filter(p -> p.getIdProducto() == idUpdate)
                                .findFirst()
                                .orElse(null);

                        if (prodUpdate != null) {
                            System.out.print("Ingresa el nuevo stock disponible: ");
                            int nuevoStock = scanner.nextInt();
                            prodUpdate.setInventario(nuevoStock);
                            System.out.println("✅ Inventario actualizado correctamente para: " + prodUpdate.getNombreProducto());
                        } else {
                            System.out.println("❌ Producto no encontrado.");
                        }
                        break;
                    case 4:
                        usuarioLogueado = null;
                        System.out.println("🔒 Sesión cerrada exitosamente.");
                        break;
                    default:
                        System.out.println("⚠️ Opción inválida.");
                }
            }

        } while (opcion != 4 || usuarioLogueado != null);

        scanner.close();
    }

    private static void mostrarCatalogo(ProductoRepository repo) {
        System.out.println("\n--- CATÁLOGO DISPONIBLE ---");
        List<Producto> productos = repo.listarProductos();
        for (Producto p : productos) {
            System.out.println("ID: " + p.getIdProducto() +
                    " | " + p.getNombreProducto() +
                    " | Precio: $" + p.getPrecio() +
                    " | Stock: " + p.getInventario());
        }
    }
}