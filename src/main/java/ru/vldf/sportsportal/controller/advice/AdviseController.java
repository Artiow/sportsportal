package ru.vldf.sportsportal.controller.advice;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.dto.handling.ErrorDTO;
import ru.vldf.sportsportal.dto.handling.ErrorMapDTO;
import ru.vldf.sportsportal.service.generic.*;

import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author Namednev Artem
 */
@Slf4j
@RestControllerAdvice
public class AdviseController {

    private final MessageContainer messages;

    @Autowired
    public AdviseController(MessageContainer messages) {
        this.messages = messages;
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleThrowable(Throwable ex) {
        return errorDTO(ex, "Unexpected internal server error");
    }

    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleJwtException(JwtException ex) {
        return errorDTO(ex, "JWT read/write error");
    }

    @ExceptionHandler(ResourceFileNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleResourceFileNotFoundException(ResourceFileNotFoundException ex) {
        return errorDTO(ex, "Requested file not found");
    }

    @ExceptionHandler(ResourceCorruptedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleResourceCorruptedException(ResourceCorruptedException ex) {
        return errorDTO(ex, "Requested resource corrupted");
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMapDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, String> errorMap = new HashMap<>(allErrors.size());
        for (ObjectError error : allErrors) {
            String code = Optional.ofNullable(error.getArguments())
                    .map(args -> ((DefaultMessageSourceResolvable) args[0]))
                    .map(DefaultMessageSourceResolvable::getCode)
                    .orElse("null");
            if (code.equals("")) code = "class";
            errorMap.put(code, error.getDefaultMessage());
        }

        Throwable cause = ex.getCause();
        String exceptionMessage = messages.get("sportsportal.handle.MethodArgumentNotValidException.message");
        String causeClassName = (cause != null) ? cause.getClass().getName() : null;
        String causeMessage = (cause != null) ? cause.getMessage() : null;
        return new ErrorMapDTO(
                warnUUID("Sent body not valid"),
                ex.getClass().getName(),
                exceptionMessage,
                causeClassName,
                causeMessage,
                errorMap
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return warnDTO(ex, "Sent argument not valid");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return warnDTO(ex, "Sent HTTP message not readable");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return warnDTO(ex, "Requested HTTP method not supported");
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return warnDTO(ex, "Sent request does not contain required parameter");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return warnDTO(ex, "Sent request argument mismatch");
    }

    @ExceptionHandler(SentDataCorruptedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleSentDataCorruptedException(SentDataCorruptedException ex) {
        return warnDTO(ex, "Sent access token not readable");
    }


    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return warnDTO(ex, "Unexpected unauthorized access attempt");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleAuthenticationException(AuthenticationException ex) {
        return warnDTO(ex, "Unauthorized access attempt");
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleSignatureException(SignatureException ex) {
        return warnDTO(ex, "Unauthorized access attempt");
    }


    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleAccountStatusException(AccountStatusException ex) {
        return warnDTO(ex, "Forbidden account status access attempt");
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO handleForbiddenAccessException(ForbiddenAccessException ex) {
        return warnDTO(ex, "Forbidden access attempt");
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return warnDTO(ex, ex.getMessage());
    }

    @ExceptionHandler(HandlerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleHandlerNotFoundException(HandlerNotFoundException ex) {
        return warnDTO(ex, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleResourceNotFoundException(ResourceNotFoundException ex) {
        return warnDTO(ex, "Requested resource not found");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return warnDTO(ex, "Requested user not found");
    }


    @ExceptionHandler(ResourceCannotCreateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleResourceCannotCreateException(ResourceCannotCreateException ex) {
        return warnDTO(ex, "Sent resource cannot create");
    }

    @ExceptionHandler(ResourceCannotUpdateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleResourceCannotUpdateException(ResourceCannotUpdateException ex) {
        return warnDTO(ex, "Sent resource cannot update");
    }

    @ExceptionHandler(ResourceOptimisticLockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO handleResourceOptimisticLockException(ResourceOptimisticLockException ex) {
        return warnDTO(ex, "Sent resource data already has been changed");
    }


    public ErrorDTO errorDTO(Throwable ex, String logMessage) {
        return new ErrorDTO(errorUUID(ex, logMessage), ex);
    }

    public UUID errorUUID(Throwable ex, String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.error(logMessage + ". UUID: {}", uuid, ex);
        return uuid;
    }

    public ErrorDTO warnDTO(Throwable ex, String logMessage) {
        return new ErrorDTO(warnUUID(ex, logMessage), ex);
    }

    public UUID warnUUID(Throwable ex, String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.warn("{} cause by {}. UUID: {}", logMessage, ex.toString(), uuid);
        return uuid;
    }

    public UUID warnUUID(String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.warn("{}. UUID: {}", logMessage, uuid);
        return uuid;
    }
}
