package ru.vldf.sportsportal.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class CommonController {

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/registration")
    public ModelAndView registration() {
        return new ModelAndView("registration");
    }

    @GetMapping("/home")
    public ModelAndView home() {
        return new ModelAndView("home");
    }
}
