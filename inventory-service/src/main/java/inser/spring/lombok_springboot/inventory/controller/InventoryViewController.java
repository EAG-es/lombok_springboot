package inser.spring.lombok_springboot.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Locale;

@Controller
@RequiredArgsConstructor
public class InventoryViewController {

    private final MessageSource messageSource;

    @GetMapping("/")
    public String index(Model model, Locale locale) {

        model.addAttribute("pageTitle", messageSource.getMessage("page.title", null, locale));
        model.addAttribute("serviceName", messageSource.getMessage("service.name", null, locale));
        model.addAttribute("welcomeMessage", messageSource.getMessage("welcome.message", null, locale));
        model.addAttribute("serviceDescription", messageSource.getMessage("service.description", null, locale));
        model.addAttribute("serviceKey", "inventory");
        model.addAttribute("lang", locale.getLanguage());
        model.addAttribute("isEn", locale.getLanguage().equals("en"));
        model.addAttribute("isEs", locale.getLanguage().equals("es"));
        model.addAttribute("isInventory", true);

        String serviceName = messageSource.getMessage("service.name", null, locale);
        model.addAttribute("opsTitle", messageSource.getMessage("ops.title", new Object[] { serviceName }, locale));
        model.addAttribute("opsCreate", messageSource.getMessage("ops.create", null, locale));
        model.addAttribute("opsRead", messageSource.getMessage("ops.read", null, locale));
        model.addAttribute("opsUpdate", messageSource.getMessage("ops.update", null, locale));
        model.addAttribute("opsDelete", messageSource.getMessage("ops.delete", null, locale));

        return "index";
    }
}
