package ru.vldf.sportsportal.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class CommonRouter {

    @GetMapping({"/", "/index"})
    public String index() {
        return "redirect:/lease";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home")
                .addObject("header_title_text", "ЛИЧНЫЙ КАБИНЕТ")
                .addObject("header_title_href", "/home")
                .addObject("header_back_text", "НА ГЛАВНУЮ")
                .addObject("header_back_href", "/");
    }
}
