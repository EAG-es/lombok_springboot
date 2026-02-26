package inser.spring.lombok_springboot.product.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProductDTO to verify Lombok annotations.
 */
public class ProductDTOTest {

    @Test
    public void testProductDTOLombokAnnotations() {
        ProductDTO dto = ProductDTO.builder()
                .id("1")
                .name("Smartphone")
                .description("Latest model")
                .price("800.00")
                .build();

        assertEquals("1", dto.getId());
        assertEquals("Smartphone", dto.getName());
        assertEquals("800.00", dto.getPrice());

        ProductDTO dto2 = new ProductDTO();
        dto2.setId("1");
        dto2.setName("Smartphone");
        dto2.setDescription("Latest model");
        dto2.setPrice("800.00");

        assertEquals(dto, dto2);
    }
}
