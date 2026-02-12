package inser.spring.lombok_springboot.order.service;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import inser.spring.lombok_springboot.order.model.Order;
import inser.spring.lombok_springboot.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import inser.spring.lombok_springboot.order.config.NullSafetyConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Implementation of OrderService.
 * Uses Lombok's @RequiredArgsConstructor and @Slf4j.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MessageSource messageSource;

    private String getMessage(String code, Object... args) {
        String message = messageSource.getMessage(code, args, "Message key not found: " + code,
                LocaleContextHolder.getLocale());
        return NullSafetyConfig.requireNonNull(message);
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(OrderDTO orderDTO) {
        log.info(getMessage("log.placing", orderDTO.getProductId()));

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .productId(orderDTO.getProductId())
                .quantity(orderDTO.getQuantity())
                .totalPrice(orderDTO.getTotalPrice())
                .build();

        Order savedOrder = orderRepository.save(order);

        return NullSafetyConfig.requireNonNull(OrderDTO.builder()
                .id(savedOrder.getId())
                .orderNumber(savedOrder.getOrderNumber())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .totalPrice(savedOrder.getTotalPrice())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        log.info(getMessage("log.fetching.id", id));
        return NullSafetyConfig.requireNonNull(orderRepository.findById(id)
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .productId(order.getProductId())
                        .quantity(order.getQuantity())
                        .totalPrice(order.getTotalPrice())
                        .build())
                .orElseThrow(() -> new RuntimeException(getMessage("order.notfound", id))));
    }

    @Override
    @Transactional
    public void cancelOrder(Long id) {
        log.info(getMessage("log.cancelling", id));
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException(getMessage("order.notfound", id));
        }
        orderRepository.deleteById(id);
    }
}
