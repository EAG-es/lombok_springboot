package innui.utils.bundles;

import innui.utils.config.NullSafetyConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Bundles {
    private static MessageSource staticMessageSource;
    private final MessageSource messageSource;

    public Bundles() {
        this.messageSource = staticMessageSource;
    }

    @Autowired
    public Bundles(MessageSource messageSource) {
        this.messageSource = messageSource;
        staticMessageSource = messageSource;
    }

    public String getMessage(String code, Object... args) {
        if (messageSource == null) {
            return "Message key not found: " + code;
        }
        String msg = messageSource.getMessage(code, args, "Message key not found: " + code,
                        LocaleContextHolder.getLocale());
        return msg != null ? msg : "Message key not found: " + code;
    }
}
