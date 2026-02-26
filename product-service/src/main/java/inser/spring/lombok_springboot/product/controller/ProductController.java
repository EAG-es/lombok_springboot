package inser.spring.lombok_springboot.product.controller;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.service.ProductService;
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
 * REST Controller for Product Service.
 * Provides endpoints for managing products.
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Controller", description = "Endpoints for managing products in the eCommerce platform")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/select")
    @Operation(summary = "Get selected products", description = "Retrieves a list of selected products")
    public ResponseEntity<PagedModel<ProductDTO>> getSelectedProducts(
            @RequestBody ProductDTO productDTO,
            @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestHeader(value = "X-Location", required = false) String location) {
        // Set locale based on location
        if (location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDTO> pageResult = productService.getSelectedProducts(productDTO, pageable);
        return ResponseEntity.ok(new PagedModel<>(pageResult));
    }

    /**
     * Retrieves product details by ID.
     * 
     * @param id The ID of the product
     * @return ProductDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves detailed information about a product by its ID")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id,
            @RequestHeader("X-Location") String location) {
        // Set locale based on location
        if (location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Adds a new product.
     * 
     * @param productDTO The product to add
     * @param location   The location header for locale setting
     * @return The created ProductDTO
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Adds a new product to the system")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO,
            @RequestHeader("X-Location") String location) {
        // Set locale based on location
        if (location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product.
     * 
     * @param id         The ID of the product to update
     * @param productDTO The updated product data
     * @param location   The location header for locale setting
     * @return The updated ProductDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO,
            @RequestHeader("X-Location") String location) {
        // Set locale based on location
        if (location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    /**
     * Deletes a product by ID.
     * 
     * @param id The ID of the product to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, @RequestHeader("X-Location") String location) {
        // Set locale based on location
        if (location.contains("lang=es")) {
            LocaleContextHolder.setLocale(new Locale("es"));
        } else {
            LocaleContextHolder.setLocale(new Locale("en"));
        }
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
