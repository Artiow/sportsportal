package ru.vldf.sportsportal.controller.advice;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.vldf.sportsportal.service.general.throwable.ForbiddenAccessException;
import ru.vldf.sportsportal.service.general.throwable.HandlerNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Optional;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@ApiIgnore
@Controller
public class SupportingController implements ErrorController {

    private static final String WELCOME_PATH = "/";
    private static final String CSRF_PATH = "/csrf";
    private static final String ERROR_PATH = "/error";

    private static final String SWAGGER_PATH = "/swagger-ui.html";

    private final String welcomeMessage;
    private final String csrfMessage;


    @Autowired
    public SupportingController(@Value("${api.title}") String title) {
        this.welcomeMessage = String.format("Welcome to %s", title);
        this.csrfMessage = "CSRF protection is disabled as unnecessary";
    }


    private static HttpStatus extractErrorStatusCode(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).map(Object::toString).map(Integer::valueOf).map(HttpStatus::resolve).orElse(HttpStatus.NOT_FOUND);
    }

    private static String extractErrorRequestPath(HttpServletRequest request) {
        return Optional.ofNullable(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI)).map(Object::toString).orElse(ERROR_PATH);
    }


    /**
     * Returns welcome message.
     *
     * @return object with message.
     */
    @ResponseBody
    @GetMapping(WELCOME_PATH)
    public Object toWelcome() {
        // noinspection unused
        return new Object() {
            @JsonProperty
            private String message = welcomeMessage;
            @JsonProperty
            private URI documentation = buildURL(SWAGGER_PATH);
        };
    }

    /**
     * Returns CSRF state message.
     *
     * @return object with message.
     */
    @ResponseBody
    @GetMapping(CSRF_PATH)
    public Object toCsrf() {
        // noinspection unused
        return new Object() {
            @JsonProperty
            private String message = csrfMessage;
        };
    }

    /**
     * Unexpected error handler.
     *
     * @param request the request that contains error status.
     * @throws Exception that corresponds to an error.
     */
    @GetMapping(ERROR_PATH)
    public void handleError(HttpServletRequest request) throws Exception {
        HttpStatus status = extractErrorStatusCode(request);
        String message = String.format("%s. Error request uri: %s", status.getReasonPhrase(), extractErrorRequestPath(request));
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
