package ru.vldf.sportsportal.controller.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.HandlerNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Namednev Artem
 */
@ApiIgnore
@Controller
public class SupportingController implements ErrorController {

    private static final String ERROR_PATH = "/error";


    /**
     * Returns CSRF state message.
     *
     * @return object with message.
     */
    @ResponseBody
    @GetMapping("/csrf")
    public Object toCsrf() {
        // noinspection unused
        return new Object() {
            @JsonProperty
            private String message = "CSRF protection is disabled as unnecessary";
        };
    }

    /**
     * Returns redirect to swagger page from associated paths.
     *
     * @return redirect string to swagger page.
     */
    @GetMapping({"/", "/swagger", "/swagger/", "/swagger-ui", "/swagger-ui/", "/swagger-ui.html/"})
    public String toSwagger() {
        return "redirect:/swagger-ui.html";
    }

    /**
     * Unexpected error handler.
     *
     * @param request the request that contains error status.
     * @throws Exception that corresponds to an error.
     */
    @GetMapping(ERROR_PATH)
    public void handleError(HttpServletRequest request) throws Exception {
        HttpStatus status = Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
                .map(Object::toString)
                .map(Integer::valueOf)
                .map(HttpStatus::resolve)
                .orElse(HttpStatus.NOT_FOUND);

        String message = String.format("%s. Error request uri: %s", status.getReasonPhrase(),
                Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI))
                        .map(Object::toString)
                        .orElse(ERROR_PATH)
        );

        switch (status) {
            case NOT_FOUND:
                throw new HandlerNotFoundException(message);
            case UNAUTHORIZED:
                throw new UnauthorizedAccessException(message);
            case FORBIDDEN:
                throw new ForbiddenAccessException(message);
            default:
                throw new Exception(message);
        }
    }

    /**
     * Returns error path.
     *
     * @return error path.
     */
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
