package inser.spring.lombok_springboot.product.config;

/**
 * Centralized null safety configuration.
 * Provides a method to ensure objects are non-null with a catch block for
 * debugging.
 */
public class NullSafetyConfig {

    /**
     * Ensures that the provided object is not null.
     * Use this method to centralize null checks and set breakpoints for debugging.
     * 
     * @param <T> Type of the object
     * @param obj The object to check
     * @return The object if not null
     * @throws NullPointerException if obj is null
     */
    @org.springframework.lang.NonNull
    public static <T> T requireNonNull(@org.springframework.lang.Nullable T obj) {
        try {
            if (obj == null) {
                throw new NullPointerException("Object must not be null");
            }
            return obj;
        } catch (NullPointerException e) {
            // Set breakpoint here to catch nullity issues during debugging
            throw e;
        }
    }
}
