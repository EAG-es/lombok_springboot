package inser.spring.lombok_springboot.order.controller;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.servlet.http.HttpServletRequest;

import inser.spring.lombok_springboot.order.OrderApplication;

@ControllerAdvice
public class SecurityControllerAdvice {

    @ModelAttribute("_csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        return OrderApplication.ensureNonNull((CsrfToken) request.getAttribute(CsrfToken.class.getName()));
    }
}
