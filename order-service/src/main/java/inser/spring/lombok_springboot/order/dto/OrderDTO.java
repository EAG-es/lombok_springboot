package inser.spring.lombok_springboot.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Order.
 * Fields are Strings to simplify UI handling and avoid type conversion issues.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private String id;
    private String orderNumber;
    private String productId;
    private String quantity;
    private String totalPrice;
}
