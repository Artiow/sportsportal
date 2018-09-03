package ru.vldf.sportsportal.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String home() {
        return "home";
    }
}
