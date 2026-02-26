package inser.spring.lombok_springboot.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

/**
 * Main application class for Inventory Service.
 */
@SpringBootApplication
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }

    /**
     * Centralized null check method for debugging purposes.
     * 
     * @param <T> Type of the object
     * @param obj The object to check
     * @return The object if not null
     * @throws NullPointerException if obj is null
     */
    @org.springframework.lang.NonNull
    public static <T> T ensureNonNull(@org.springframework.lang.Nullable T obj) {
        return Objects.requireNonNull(obj);
    }
}
