package inser.spring.lombok_springboot.product.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductDTO to verify Lombok annotations.
 */
public class ProductDTOTest {

    @Test
    public void testProductDTOLombokAnnotations() {
        ProductDTO dto = ProductDTO.builder()
                .id(1L)
                .name("Smartphone")
                .description("Latest model")
                .price(new BigDecimal("800.00"))
                .build();

        assertEquals(1L, dto.getId());
        assertEquals("Smartphone", dto.getName());

        ProductDTO dto2 = new ProductDTO();
        dto2.setId(1L);
        dto2.setName("Smartphone");
        dto2.setDescription("Latest model");
        dto2.setPrice(new BigDecimal("800.00"));

        assertEquals(dto, dto2); // Verifies @Data
    }
}
