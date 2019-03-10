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
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.dto.handling.ErrorDTO;
import ru.vldf.sportsportal.dto.handling.ErrorMapDTO;
import ru.vldf.sportsportal.service.filesystem.PictureFileException;
import ru.vldf.sportsportal.service.generic.*;

import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author Namednev Artem
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("SameParameterValue")
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

    @ExceptionHandler(PictureFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handlePictureFileException(PictureFileException ex) {
        return errorDTO(ex, "Requested picture file not found");
    }

    @ExceptionHandler(ResourceCorruptedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleResourceCorruptedException(ResourceCorruptedException ex) {
        return errorDTO(ex, "Requested resource corrupted");
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException.class)
    public ErrorDTO handleInvalidParameterException(InvalidParameterException ex) {
        return warnDTO(ex, "Sent argument not valid");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return warnDTO(ex, "Sent argument not valid");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return warnDTO(ex, "Sent HTTP message not readable");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorDTO handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        return warnDTO(ex, "Requested HTTP method not supported");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ErrorDTO handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return warnDTO(ex, "Load file size is too large");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorDTO handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return warnDTO(ex, "Sent request does not contain required parameter");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return warnDTO(ex, "Sent request argument mismatch");
    }


    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ErrorDTO handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return warnDTO(ex, "Unexpected unauthorized access attempt");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorDTO handleAuthenticationException(AuthenticationException ex) {
        return warnDTO(ex, "Unauthorized access attempt");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SignatureException.class)
    public ErrorDTO handleSignatureException(SignatureException ex) {
        return warnDTO(ex, "Unauthorized access attempt");
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccountStatusException.class)
    public ErrorDTO handleAccountStatusException(AccountStatusException ex) {
        return warnDTO(ex, "Forbidden account status access attempt");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenAccessException.class)
    public ErrorDTO handleForbiddenAccessException(ForbiddenAccessException ex) {
        return warnDTO(ex, "Forbidden access attempt");
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorDTO handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return warnDTO(ex, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(HandlerNotFoundException.class)
    public ErrorDTO handleHandlerNotFoundException(HandlerNotFoundException ex) {
        return warnDTO(ex, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorDTO handleResourceNotFoundException(ResourceNotFoundException ex) {
        return warnDTO(ex, "Requested resource not found");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UsernameNotFoundException.class)
    public ErrorDTO handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return warnDTO(ex, "Requested user not found");
    }


    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceCannotCreateException.class)
    public ErrorDTO handleResourceCannotCreateException(ResourceCannotCreateException ex) {
        return warnDTO(ex, "Sent resource cannot create");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceCannotUpdateException.class)
    public ErrorDTO handleResourceCannotUpdateException(ResourceCannotUpdateException ex) {
        return warnDTO(ex, "Sent resource cannot update");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ResourceOptimisticLockException.class)
    public ErrorDTO handleResourceOptimisticLockException(ResourceOptimisticLockException ex) {
        return warnDTO(ex, "Sent resource data already has been changed");
    }


    public ErrorDTO errorDTO(Throwable ex, String logMessage) {
        return new ErrorDTO(errorUUID(ex, logMessage), ex);
    }

    public ErrorDTO warnDTO(Throwable ex, String logMessage) {
        return new ErrorDTO(warnUUID(ex, logMessage), ex);
    }


    private UUID errorUUID(Throwable ex, String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.error(logMessage + ". UUID: {}", uuid, ex);
        return uuid;
    }

    private UUID warnUUID(Throwable ex, String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.warn("{}. Cause by {}. UUID: {}", logMessage, ex.toString(), uuid);
        return uuid;
    }

    private UUID warnUUID(String logMessage) {
        UUID uuid = UUID.randomUUID();
        log.warn("{}. UUID: {}", logMessage, uuid);
        return uuid;
    }
}
