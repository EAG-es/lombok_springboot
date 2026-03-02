package inser.spring.lombok_springboot.order.service;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service interface for managing Orders.
 * Follows the pattern established in the product-service.
 */
public interface OrderService {
    List<OrderDTO> getAllOrders();

    Page<OrderDTO> getSelectedOrders(OrderDTO orderDTO, Pageable pageable);

    OrderDTO getOrderById(Long id);

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Long id, OrderDTO orderDTO);

    void deleteOrder(Long id);
}
