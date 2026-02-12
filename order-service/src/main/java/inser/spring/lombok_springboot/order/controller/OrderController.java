package inser.spring.lombok_springboot.order.controller;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import inser.spring.lombok_springboot.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Order Service.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Places a new order.
     * 
     * @param orderDTO The order data
     * @return OrderDTO
     */
    @PostMapping
    @Operation(summary = "Place a new order", description = "Creates a new order in the system")
    public ResponseEntity<OrderDTO> placeOrder(@RequestBody OrderDTO orderDTO) {
        return new ResponseEntity<>(orderService.placeOrder(orderDTO), HttpStatus.CREATED);
    }

    /**
     * Retrieves details of an order.
     * 
     * @param id The ID of the order
     * @return OrderDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves details of a specific order by its ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Cancels an order.
     * 
     * @param id The ID of the order to cancel
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel an order", description = "Deletes an order from the system")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
