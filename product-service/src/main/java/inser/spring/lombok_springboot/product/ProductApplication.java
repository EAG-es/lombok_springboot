package inser.spring.lombok_springboot.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.Objects;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
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
