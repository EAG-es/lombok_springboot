package inser.spring.lombok_springboot.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Product.
 * Facilitates communication between layers and API clients.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private String price;
}
