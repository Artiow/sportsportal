package ru.vldf.sportsportal.controller.advice;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.vldf.sportsportal.dto.handling.ErrorDTO;
import ru.vldf.sportsportal.service.generic.AuthorizationRequiredException;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.HandlerNotFoundException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@ApiIgnore
@Controller
public class RouterController implements ErrorController {

    @GetMapping({"/", "/swagger", "/swagger/", "/swagger-ui", "/swagger-ui/", "/swagger-ui.html/"})
    public String toSwagger() {
        return "redirect:/swagger-ui.html";
    }

    @ResponseBody
    @GetMapping("/error")
    public ErrorDTO handleError(HttpServletRequest request) throws Exception {
        HttpStatus status = Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
                .map(Object::toString)
                .map(Integer::valueOf)
                .map(HttpStatus::resolve)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        String reasonPhrase = status.getReasonPhrase();
        switch (status) {
            case NOT_FOUND: {
                throw new HandlerNotFoundException(reasonPhrase);
            }
            case UNAUTHORIZED: {
                throw new AuthorizationRequiredException(reasonPhrase);
            }
            case FORBIDDEN: {
                throw new ForbiddenAccessException(reasonPhrase);
            }
            default: {
                throw new Exception(reasonPhrase);
            }
        }
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
