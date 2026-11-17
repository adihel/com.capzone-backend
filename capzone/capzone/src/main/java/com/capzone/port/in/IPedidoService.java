package com.capzone.port.in;

import com.capzone.domain.Pedido;
import com.capzone.domain.Usuario;

import java.util.List;
import java.util.Map;

public interface IPedidoService {
    /** items: idProducto -> cantidad. Todo o nada: si un producto falla, no se descuenta nada. */
    Pedido crearPedido(Usuario usuario, Map<Integer, Integer> items);
    List<Pedido> historialDe(Usuario usuario);
}
