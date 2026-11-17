package com.capzone.service;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.EstadoPedido;
import com.capzone.domain.Pedido;
import com.capzone.domain.Producto;
import com.capzone.domain.Usuario;
import com.capzone.exception.RecursoNoEncontradoException;
import com.capzone.exception.StockInsuficienteException;
import com.capzone.port.in.IPedidoService;
import com.capzone.port.out.IPedidoRepository;
import com.capzone.port.out.IProductoRepository;
import com.capzone.utils.Validador;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PedidoService implements IPedidoService {
    private final IPedidoRepository pedidoRepository;
    private final IProductoRepository productoRepository;

    public PedidoService(IPedidoRepository pedidoRepository, IProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public Pedido crearPedido(Usuario usuario, Map<Integer, Integer> items) {
        if (usuario == null) {
            throw new IllegalStateException("Debes iniciar sesión para comprar.");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un producto.");
        }

        // 1) Validar todo y armar el pedido (aún no se toca la BD)
        Pedido pedido = new Pedido(0, usuario, LocalDateTime.now(), EstadoPedido.CONFIRMADO, BigDecimal.ZERO);
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Integer, Integer> item : items.entrySet()) {
            int cantidad = item.getValue();
            Validador.positivo(cantidad, "La cantidad");

            Producto producto = productoRepository.buscarPorId(item.getKey())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto", item.getKey()));

            if (!producto.hayStock(cantidad)) {
                throw new StockInsuficienteException(producto.getNombreProducto(), producto.getInventario(), cantidad);
            }
            DetallePedido detalle = new DetallePedido(0, pedido, producto, cantidad, producto.getPrecio());
            detalles.add(detalle);
            total = total.add(detalle.calcularSubtotal());
        }
        pedido.setTotal(total);

        // 2) Guardar: el repositorio inserta pedido + detalles y descuenta el stock en UNA transacción
        pedidoRepository.guardar(pedido, detalles);
        return pedido;
    }

    @Override
    public List<Pedido> historialDe(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalStateException("Debes iniciar sesión para ver tu historial.");
        }
        return pedidoRepository.listarPorUsuario(usuario.getIdUsuario());
    }
}
