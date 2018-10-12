package ru.vldf.sportsportal.controller;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.security.TokenDTO;
import ru.vldf.sportsportal.service.AuthService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.SentDataCorruptedException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

@RestController
@Api(tags = {"Authentication"})
@RequestMapping("${api.path.common.auth}")
public class AuthController {

    @Value("${api.path.common.user}")
    private String userPath;

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }


    /**
     * Returns token by logged user.
     *
     * @param email    {@link String} users email
     * @param password {@link String} users password
     * @return {@link TokenDTO} token data
     * @throws UsernameNotFoundException if user not found
     * @throws JwtException              if could not parse jwt
     */
    @GetMapping("/login")
    @ApiOperation("получить токен")
    public TokenDTO login(@RequestParam String email, @RequestParam String password)
            throws UsernameNotFoundException, JwtException {
        return authService.login(email, password);
    }

    /**
     * Returns token by users access token.
     *
     * @param accessToken {@link String} users access token
     * @return {@link TokenDTO} token data
     * @throws UsernameNotFoundException  if user not found
     * @throws ResourceNotFoundException  if user not found
     * @throws SentDataCorruptedException if token not valid
     * @throws JwtException               if could not parse jwt
     */
    @GetMapping("/verify")
    @ApiOperation("верификация")
    public TokenDTO verify(@RequestParam String accessToken)
            throws UsernameNotFoundException, ResourceNotFoundException, SentDataCorruptedException, JwtException {
        return authService.verify(accessToken);
    }

    /**
     * Register new user.
     *
     * @param userDTO {@link UserDTO} new user data
     * @return new user location
     * @throws ResourceCannotCreateException if user cannot create
     */
    @PostMapping("/register")
    @ApiOperation("регистрация")
    public ResponseEntity<Void> register(@RequestBody @Validated(UserDTO.CreateCheck.class) UserDTO userDTO)
            throws ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(userPath, authService.register(userDTO))).build();
    }

    /**
     * Init confirmation for user and send him confirmation email.
     *
     * @param id   user identifier
     * @param host {@link String} confirmation link host
     * @return no content
     * @throws ResourceNotFoundException     if user could not found
     * @throws ResourceCannotUpdateException if could not sent email
     */
    @PutMapping("/confirm/{id}")
    @ApiOperation("отправить письмо для подтверждения электронной почты")
    public ResponseEntity<Void> confirm(@PathVariable int id, @RequestParam String host) throws ResourceNotFoundException, ResourceCannotUpdateException {
        authService.initConfirmation(id, host);
        return ResponseEntity.noContent().build();
    }

    /**
     * User confirmation.
     *
     * @param confirmToken @link String} user's confirmation token
     * @return no content
     * @throws ResourceNotFoundException if user not found by confirm code
     */
    @PutMapping("/confirm")
    @ApiOperation("подтвердить пользователя")
    public ResponseEntity<Void> confirm(@RequestParam String confirmToken) throws ResourceNotFoundException {
        authService.confirm(confirmToken);
        return ResponseEntity.noContent().build();
    }
}
