package inser.spring.lombok_springboot.order.service;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import inser.spring.lombok_springboot.order.mapper.OrderMapper;
import inser.spring.lombok_springboot.order.model.Order;
import inser.spring.lombok_springboot.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import innui.utils.Numbers;
import innui.utils.bundles.Bundles;
import innui.utils.config.NullSafetyConfig;
import innui.utils.sql.Sql;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;

/**
 * Implementation of OrderService.
 * Follows the pattern established in the product-service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final Bundles bundle;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        log.info(bundle.getMessage("log.fetching.all"));
        return NullSafetyConfig.requireNonNull(orderRepository.findAll().stream()
                .map(i -> orderMapper.toDTO(i))
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getSelectedOrders(OrderDTO orderDTO, Pageable pageable) {
        log.info(bundle.getMessage("log.fetching.selected"));
        String where = "";
        if (orderDTO.getId() != null && !orderDTO.getId().isBlank()) {
            where = Sql.createAndWhere(where, "id", orderDTO.getId(), false);
        }
        if (orderDTO.getOrderNumber() != null && !orderDTO.getOrderNumber().isBlank()) {
            where = Sql.createAndWhere(where, "order_number", orderDTO.getOrderNumber(), true);
        }
        if (orderDTO.getProductId() != null && !orderDTO.getProductId().isBlank()) {
            where = Sql.createAndWhere(where, "product_id", orderDTO.getProductId(), false);
        }
        if (orderDTO.getQuantity() != null && !orderDTO.getQuantity().isBlank()) {
            where = Sql.createAndWhere(where, "quantity", orderDTO.getQuantity(), false);
        }
        if (orderDTO.getTotalPrice() != null && !orderDTO.getTotalPrice().isBlank()) {
            where = Sql.createAndWhere(where, "total_price", orderDTO.getTotalPrice(), false);
        }
        return orderRepository.findByWhere(where, pageable)
                .map(i -> orderMapper.toDTO(i));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.info(bundle.getMessage("log.fetching.id", id));
        return NullSafetyConfig.requireNonNull(orderRepository.findById(id)
                .map(i -> orderMapper.toDTO(i))
                .orElseThrow(() -> new RuntimeException(bundle.getMessage("order.notfound", id))));
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public OrderDTO createOrder(OrderDTO orderDTO) {
        log.info(bundle.getMessage("log.creating", orderDTO.getOrderNumber()));

        if (orderDTO.getId() != null
                && !orderDTO.getId().isBlank()
                && orderRepository.existsById(Numbers.parseLong(orderDTO.getId()))) {
            throw new RuntimeException(bundle.getMessage("order.exists", orderDTO.getId()));
        }

        Order order = NullSafetyConfig.requireNonNull(orderMapper.toEntity(orderDTO));
        // Business logic: Generate order number if not present
        if (order.getOrderNumber() == null || order.getOrderNumber().isBlank()) {
            order.setOrderNumber("ORD-" + System.currentTimeMillis());
        }
        order.setId(null);
        Order savedOrder = orderRepository.save(NullSafetyConfig.requireNonNull(order));
        return NullSafetyConfig.requireNonNull(orderMapper.toDTO(savedOrder));
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        log.info(bundle.getMessage("log.updating", id));
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(bundle.getMessage("order.notfound", id)));

        if (orderDTO.getOrderNumber() != null && !orderDTO.getOrderNumber().isBlank()) {
            existingOrder.setOrderNumber(orderDTO.getOrderNumber());
        }
        if (orderDTO.getProductId() != null && !orderDTO.getProductId().isBlank()) {
            existingOrder.setProductId(Numbers.parseLong(orderDTO.getProductId()));
        }
        if (orderDTO.getQuantity() != null && !orderDTO.getQuantity().isBlank()) {
            existingOrder.setQuantity(Numbers.parseInt(orderDTO.getQuantity()));
        }
        if (orderDTO.getTotalPrice() != null && !orderDTO.getTotalPrice().isBlank()) {
            existingOrder.setTotalPrice(BigDecimal.valueOf(Numbers.parseDouble(orderDTO.getTotalPrice())));
        }

        Order updatedOrder = orderRepository.save(existingOrder);
        return NullSafetyConfig.requireNonNull(orderMapper.toDTO(updatedOrder));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        log.info(bundle.getMessage("log.deleting", id));
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException(bundle.getMessage("order.notfound", id));
        }
        orderRepository.deleteById(id);
    }
}
