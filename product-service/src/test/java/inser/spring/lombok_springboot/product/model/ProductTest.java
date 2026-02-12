package inser.spring.lombok_springboot.product.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Product model to verify Lombok annotations.
 */
public class ProductTest {

    @Test
    public void testProductLombokAnnotations() {
        Product product = Product.builder()
                .id(1L)
                .name("Laptop")
                .description("High performance laptop")
                .price(new BigDecimal("1200.00"))
                .build();

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("High performance laptop", product.getDescription());
        assertEquals(new BigDecimal("1200.00"), product.getPrice());

        Product product2 = new Product(1L, "Laptop", "High performance laptop", new BigDecimal("1200.00"));
        assertEquals(product, product2); // Verifies @Data (equals/hashCode)
        assertEquals(product.hashCode(), product2.hashCode());
    }
}
