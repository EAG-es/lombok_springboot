package inser.spring.lombok_springboot.order.service;

import inser.spring.lombok_springboot.order.dto.OrderDTO;

/**
 * Service interface for managing Orders.
 */
public interface OrderService {
    OrderDTO placeOrder(OrderDTO orderDTO);

    OrderDTO getOrderById(Long id);

    void cancelOrder(Long id);
}
