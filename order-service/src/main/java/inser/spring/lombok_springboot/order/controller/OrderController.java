package inser.spring.lombok_springboot.order.controller;

import inser.spring.lombok_springboot.order.dto.OrderDTO;
import inser.spring.lombok_springboot.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedModel;

import java.util.Locale;

/**
 * REST Controller for Order Service.
 * Provides endpoints for managing customer orders.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Controller", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/select")
    @Operation(summary = "Get selected orders", description = "Retrieves a list of selected orders")
    public ResponseEntity<PagedModel<OrderDTO>> getSelectedOrders(
            @RequestBody OrderDTO orderDTO,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestHeader(value = "X-Location", required = false) String location) {

        // Set locale based on location
        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<OrderDTO> pageResult = orderService.getSelectedOrders(orderDTO, pageable);
        return ResponseEntity.ok(new PagedModel<>(pageResult));
    }

    /**
     * Retrieves order details by ID.
     * 
     * @param id The ID of the order
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves detailed information about an order by its ID")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    /**
     * Adds a new order.
     * 
     * @param orderDTO The order to add
     */
    @PostMapping
    @Operation(summary = "Create a new order", description = "Adds a new order to the system")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return new ResponseEntity<>(orderService.createOrder(orderDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing order.
     * 
     * @param id       The ID of the order record
     * @param orderDTO The updated order data
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        return ResponseEntity.ok(orderService.updateOrder(id, orderDTO));
    }

    /**
     * Deletes an order record by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id,
            @RequestHeader(value = "X-Location", required = false) String location) {

        if (location != null && location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }

        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
