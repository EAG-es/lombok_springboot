package inser.spring.lombok_springboot.product.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import inser.spring.lombok_springboot.product.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/test-data.sql", executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("null")
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
    }

    @Test
    public void testCreateAndGetProduct() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("99.99"))
                .build();

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Test Product")))
                .andExpect(jsonPath("$.price", is(99.99)));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Test Product")));
    }
}
