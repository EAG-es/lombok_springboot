package inser.spring.lombok_springboot.product.controller;

import inser.spring.lombok_springboot.product.dto.ProductDTO;
import inser.spring.lombok_springboot.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * Retrieves all products.
     * 
     * @return List of ProductDTO
     */
    @GetMapping
    @Operation(summary = "Get all products", description = "Retrieves a list of all available products")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Retrieves product details by ID.
     * 
     * @param id The ID of the product
     * @return ProductDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves detailed information about a product by its ID")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Adds a new product.
     * 
     * @param productDTO The product to add
     * @return The created ProductDTO
     */
    @PostMapping
    @Operation(summary = "Create a new product", description = "Adds a new product to the system")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }

    /**
     * Updates an existing product.
     * 
     * @param id         The ID of the product to update
     * @param productDTO The updated product data
     * @return The updated ProductDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }

    /**
     * Deletes a product by ID.
     * 
     * @param id The ID of the product to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
