package inser.spring.lombok_springboot.product.mapper;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.model.Product;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Product entity and ProductDTO.
 */
@Component
public class ProductMapper {

    @Nullable
    public ProductDTO toDTO(@Nullable Product product) {
        if (product == null)
            return null;
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    @Nullable
    public Product toEntity(@Nullable ProductDTO dto) {
        if (dto == null)
            return null;
        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }
}
