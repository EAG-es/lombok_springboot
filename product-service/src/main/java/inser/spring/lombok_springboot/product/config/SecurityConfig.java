package inser.spring.lombok_springboot.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .authorizeHttpRequests(auth -> auth
                         .anyRequest().permitAll())
                 .csrf(csrf -> csrf.ignoringRequestMatchers("/products/**"))
                 // CSRF is disabled for API endpoints under /products
                 .formLogin(form -> form.disable())
                 .httpBasic(basic -> basic.disable());

         return http.build();
     }
}
