package inser.spring.lombok_springboot.order.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import inser.spring.lombok_springboot.order.dto.OrderDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@org.springframework.test.context.jdbc.Sql(scripts = "/test-data.sql", executionPhase = org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SuppressWarnings("null")
public class OrderIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        void setup() {
        }

        @Test
        public void testCreateAndGetOrder() throws Exception {
                OrderDTO orderDTO = OrderDTO.builder()
                                .productId("1")
                                .quantity("5")
                                .totalPrice("500.00")
                                .build();

                mockMvc.perform(post("/orders")
                                .header("X-Location", "US")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(orderDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.orderNumber", notNullValue()))
                                .andExpect(jsonPath("$.productId", is("1")));

                mockMvc.perform(post("/orders/select")
                                .header("X-Location", "US")
                                .param("page", "0")
                                .param("pageSize", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)))
                                .andExpect(jsonPath("$.content[1].productId", is("1")));
        }
}
