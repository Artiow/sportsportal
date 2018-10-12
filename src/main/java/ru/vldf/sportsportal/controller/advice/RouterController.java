package ru.vldf.sportsportal.controller.advice;

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

@ApiIgnore
@Controller
public class RouterController implements ErrorController {

    /**
     * Returns redirect to swagger page from associated paths.
     *
     * @return redirect to swagger page
     */
    @GetMapping({"/", "/swagger", "/swagger/", "/swagger-ui", "/swagger-ui/", "/swagger-ui.html/"})
    public String toSwagger() {
        return "redirect:/swagger-ui.html";
    }

    /**
     * Unexpected error handler.
     *
     * @param request {@link HttpServletRequest} that contains error status
     * @throws Exception that corresponds to an error
     */
    @ResponseBody
    @GetMapping("/error")
    public void handleError(HttpServletRequest request) throws Exception {
        HttpStatus status = Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE))
                .map(Object::toString)
                .map(Integer::valueOf)
                .map(HttpStatus::resolve)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        String message = String.format("%s. Error request: %s", status.getReasonPhrase(),
                Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).map(Object::toString).orElse("undefined uri")
        );
        switch (status) {
            case NOT_FOUND: {
                throw new HandlerNotFoundException(message);
            }
            case UNAUTHORIZED: {
                throw new UnauthorizedAccessException(message);
            }
            case FORBIDDEN: {
                throw new ForbiddenAccessException(message);
            }
            default: {
                throw new Exception(message);
            }
        }
    }

    /**
     * Returns error api path.
     *
     * @return {@link String} error path
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
