package innui.utils.config;

import innui.utils.bundles.Bundles;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
    @NonNull
    public static <T> T requireNonNull(@Nullable T obj) {
        if (obj == null) {
            Bundles bundles = new Bundles();
            throw new NullPointerException(bundles.getMessage("error.null.object"));
        }
        return obj;
    }
}
