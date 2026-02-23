package inser.spring.lombok_springboot.inventory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    @org.springframework.lang.NonNull
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver("lang");
        clr.setDefaultLocale(Locale.ENGLISH);
        return clr;
    }

    @Bean
    @org.springframework.lang.NonNull
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(@org.springframework.lang.NonNull InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
