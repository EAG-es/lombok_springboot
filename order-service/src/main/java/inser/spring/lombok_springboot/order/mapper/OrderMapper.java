package inser.spring.lombok_springboot.order.mapper;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import inser.spring.lombok_springboot.order.model.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import innui.utils.Numbers;
import java.math.BigDecimal;

/**
 * Mapper for converting between Order entity and OrderDTO.
 */
@Component
public class OrderMapper {

    @Nullable
    public OrderDTO toDTO(@Nullable Order order) {
        if (order == null)
            return null;
        return OrderDTO.builder()
                .id(order.getId() != null ? order.getId().toString() : null)
                .orderNumber(order.getOrderNumber())
                .productId(order.getProductId() != null ? order.getProductId().toString() : null)
                .quantity(order.getQuantity() != null ? order.getQuantity().toString() : null)
                .totalPrice(order.getTotalPrice() != null ? order.getTotalPrice().toString() : null)
                .build();
    }

    @Nullable
    public Order toEntity(@Nullable OrderDTO dto) {
        if (dto == null)
            return null;
        return Order.builder()
                .id(dto.getId() != null && !dto.getId().isBlank() ? Numbers.parseLong(dto.getId()) : null)
                .orderNumber(dto.getOrderNumber())
                .productId(dto.getProductId() != null && !dto.getProductId().isBlank()
                        ? Numbers.parseLong(dto.getProductId())
                        : null)
                .quantity(
                        dto.getQuantity() != null && !dto.getQuantity().isBlank() ? Numbers.parseInt(dto.getQuantity())
                                : null)
                .totalPrice(dto.getTotalPrice() != null && !dto.getTotalPrice().isBlank()
                        ? BigDecimal.valueOf(Numbers.parseDouble(dto.getTotalPrice()))
                        : null)
                .build();
    }
}
