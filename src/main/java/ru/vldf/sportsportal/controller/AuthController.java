package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.security.EmailHolderDTO;
import ru.vldf.sportsportal.dto.security.JwtPairDTO;
import ru.vldf.sportsportal.dto.security.PasswordHolderDTO;
import ru.vldf.sportsportal.service.AuthService;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Authentication"})
@RequestMapping("${api.path.common.auth}")
public class AuthController {

    private final AuthService authService;

    @Value("${api.path.common.user}")
    private String userPath;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Returns user token pair (access and refresh).
     *
     * @return token pair.
     * @throws UnauthorizedAccessException if user authorization is missing.
     */
    @GetMapping({"/login", "/refresh"})
    @ApiOperation("получить пару токенов")
    public JwtPairDTO login() throws UnauthorizedAccessException {
        return authService.login();
    }


    /**
     * Register new user and returns its location.
     *
     * @param userDTO the created user details.
     * @return created user location.
     * @throws ResourceCannotCreateException if user cannot be created.
     */
    @PostMapping("/register")
    @ApiOperation("регистрация")
    public ResponseEntity<Void> register(
            @RequestBody @Validated(UserDTO.RegisterCheck.class) UserDTO userDTO
    ) throws ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(userPath, authService.register(userDTO))).build();
    }

    /**
     * Initiate user confirmation and send confirmation email.
     *
     * @param id     the user identifier.
     * @param origin the confirmation link origin.
     * @return no content.
     * @throws ResourceNotFoundException     if could not found user.
     * @throws ResourceCannotUpdateException if could not sent email.
     */
    @PutMapping("/confirm/{id}")
    @ApiOperation("отправить письмо для подтверждения электронной почты")
    public ResponseEntity<Void> confirm(
            @PathVariable int id, @RequestParam String origin
    ) throws ResourceNotFoundException, ResourceCannotUpdateException {
        authService.confirmationInit(id, origin);
        return ResponseEntity.noContent().build();
    }

    /**
     * Confirm user.
     *
     * @param token the user confirmation token.
     * @return no content.
     * @throws ResourceNotFoundException if user not found by confirm code.
     */
    @PutMapping("/confirm")
    @ApiOperation("подтвердить пользователя")
    public ResponseEntity<Void> confirm(
            @RequestParam String token
    ) throws ResourceNotFoundException {
        authService.confirmationAct(token);
        return ResponseEntity.noContent().build();
    }


    /**
     * Initiate user password recovery and send recovery email.
     *
     * @param origin         the recovery link origin.
     * @param emailHolderDTO the user email holder.
     * @return no content.
     * @throws ResourceNotFoundException     if could not found user.
     * @throws ResourceCannotUpdateException if could not sent email.
     */
    @PutMapping("/recovery-init")
    @ApiOperation("отправить письмо для восстановления пароля")
    public ResponseEntity<Void> recovery(
            @RequestParam String origin, @RequestBody @Validated EmailHolderDTO emailHolderDTO
    ) throws ResourceNotFoundException, ResourceCannotUpdateException {
        authService.recoveryInit(origin, emailHolderDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recover user password.
     *
     * @param token             the user recovery token.
     * @param passwordHolderDTO the user password holder.
     * @return no content.
     * @throws ResourceNotFoundException if user not found by recovery code.
     */
    @PutMapping("/recovery-act")
    @ApiOperation("восстановить пароль пользователя")
    public ResponseEntity<Void> recovery(
            @RequestParam String token, @RequestBody @Validated PasswordHolderDTO passwordHolderDTO
    ) throws ResourceNotFoundException {
        authService.recoveryAct(token, passwordHolderDTO);
        return ResponseEntity.noContent().build();
    }
}
