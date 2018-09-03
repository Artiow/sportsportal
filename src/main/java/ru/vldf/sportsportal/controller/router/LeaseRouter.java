package ru.vldf.sportsportal.controller.router;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class LeaseRouter {

    @GetMapping("/lease")
    public String index() {
        return "lease";
    }
}
