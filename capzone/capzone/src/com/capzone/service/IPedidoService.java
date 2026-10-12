package com.capzone.service;

import com.capzone.domain.DetallePedido;
import com.capzone.domain.Pedido;
import java.util.List;

public interface IPedidoService {
    void procesarPedido(Pedido pedido, List<DetallePedido> detalles);
}