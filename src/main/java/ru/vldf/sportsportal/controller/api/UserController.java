package ru.vldf.sportsportal.controller.api;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.common.UserDTO;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.dto.security.TokenDTO;
import ru.vldf.sportsportal.service.UserService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURI;

@RestController
@RequestMapping("${api-path.common.user}")
public class UserController {

    @Value("${api-path.common.user}")
    private String apiPath;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * Returns user data by sent id.
     *
     * @param id - user id
     * @return user dto
     * @throws ResourceNotFoundException - if user not found
     */
    @GetMapping("/{id}")
    public UserShortDTO get(@PathVariable Integer id) throws ResourceNotFoundException {
        return userService.get(id);
    }

    /**
     * Returns TokenDTO by logged user.
     *
     * @param login    - user's login
     * @param password - user's password
     * @return token data
     */
    @GetMapping("/login")
    public TokenDTO login(@RequestParam String login, @RequestParam String password)
            throws UsernameNotFoundException, JwtException {
        return userService.login(login, password);
    }

    /**
     * Register new user.
     *
     * @param userDTO - new user data
     * @return new user location
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Validated(UserDTO.CreateCheck.class) UserDTO userDTO)
            throws ResourceCannotCreateException {
        return ResponseEntity.created(buildURI(apiPath, userService.register(userDTO))).build();
    }
}
