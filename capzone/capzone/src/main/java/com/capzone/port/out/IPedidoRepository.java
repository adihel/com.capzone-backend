package com.capzone.port.out;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.Pedido;

import java.util.List;

public interface IPedidoRepository {
    /** Guarda pedido + detalles y descuenta el inventario, todo en una sola transacción. */
    void guardar(Pedido pedido, List<DetallePedido> detalles);
    List<Pedido> listarPorUsuario(int idUsuario);
    List<DetallePedido> listarDetalles(Pedido pedido);
}
