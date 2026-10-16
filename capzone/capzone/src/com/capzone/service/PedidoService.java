package com.capzone.service;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.Pedido;
import com.capzone.domain.Producto;
import com.capzone.utils.EstadoPedido;

import java.math.BigDecimal;
import java.util.List;

public class PedidoService implements IPedidoService {

    @Override
    public void procesarPedido(Pedido pedido, List<DetallePedido> detalles) {
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (DetallePedido detalle : detalles) {
            Producto producto = detalle.getProducto();

            if (producto.getInventario() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombreProducto());
            }

            producto.setInventario(producto.getInventario() - detalle.getCantidad());
            totalCalculado = totalCalculado.add(detalle.calcularSubtotal());
        }

        pedido.setTotal(totalCalculado);
        // Asignamos el estado utilizando nuestro Enum y el método getDescripcion()
        pedido.setEstadoPedido(EstadoPedido.CONFIRMADO.getDescripcion());
    }
}