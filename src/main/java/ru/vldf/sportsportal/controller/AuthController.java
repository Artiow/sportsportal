package ru.vldf.sportsportal.controller;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.security.TokenDTO;
import ru.vldf.sportsportal.service.UserService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

@RestController
@Api(tags = {"Authentication"})
@RequestMapping("${api-path.common.auth}")
public class AuthController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * Returns token by logged user.
     *
     * @param login    {@link String} users login
     * @param password {@link String} users password
     * @return {@link TokenDTO} token data
     */
    @GetMapping("/login")
    @ApiOperation("получить токен")
    public TokenDTO login(@RequestParam String login, @RequestParam String password)
            throws UsernameNotFoundException, JwtException {
        return userService.login(login, password);
    }

    /**
     * Register new user.
     *
     * @param userDTO new user data
     * @return new user location
     */
    @PostMapping("/register")
    @ApiOperation("регистрация")
    public ResponseEntity<Void> register(@RequestBody @Validated(UserDTO.CreateCheck.class) UserDTO userDTO)
            throws ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(userService.register(userDTO))).build();
    }
}
