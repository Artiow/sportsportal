package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.service.UserService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"User"})
@RequestMapping("${api.path.common.user}")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Returns short user data by sent his id.
     *
     * @param id user identifier
     * @return {@link UserShortDTO} user dto
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this user data
     * @throws ResourceNotFoundException   if user not found
     */
    @GetMapping("/{id}")
    @ApiOperation("получить пользователя")
    public UserShortDTO get(@PathVariable int id)
            throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        return userService.get(id);
    }

    /**
     * Delete user by sent user id.
     *
     * @param id user identifier
     * @throws ResourceNotFoundException   if user not found
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this user
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить пользователя")
    public ResponseEntity<Void> delete(@PathVariable int id) throws ResourceNotFoundException, UnauthorizedAccessException, ForbiddenAccessException {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
