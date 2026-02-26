package inser.spring.lombok_springboot.product.mapper;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.model.Product;

import java.math.BigDecimal;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import innui.utils.Numbers;

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
                .id(product.getId().toString())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().toString())
                .build();
    }

    @Nullable
    public Product toEntity(@Nullable ProductDTO dto) {
        if (dto == null)
            return null;
        return Product.builder()
                .id(dto.getId() != null ? Numbers.parseLong(dto.getId()) : null)
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice() != null ? BigDecimal.valueOf(Numbers.parseDouble(dto.getPrice())) : null)
                .build();
    }
}
