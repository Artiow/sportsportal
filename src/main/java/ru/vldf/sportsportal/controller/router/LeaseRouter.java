package ru.vldf.sportsportal.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class LeaseRouter {

    @GetMapping("/lease")
    public ModelAndView index() {
        return new ModelAndView("lease/lease")
                .addObject("header_title_text", "АРЕНДА ПЛОЩАДОК")
                .addObject("header_title_href", "/lease")
                .addObject("header_back_text", "НА ГЛАВНУЮ")
                .addObject("header_back_href", "/");
    }
}
