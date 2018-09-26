package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.sectional.common.shortcut.UserShortDTO;
import ru.vldf.sportsportal.service.UserService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

@RestController
@Api(tags = {"User"})
@RequestMapping("${api.path.common.user}")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * Returns short user data by sent his id.
     *
     * @param id {@link Integer} user id
     * @return {@link UserShortDTO} user dto
     * @throws ResourceNotFoundException if user not found
     */
    @GetMapping("/{id}")
    @ApiOperation("получить пользователя")
    public UserShortDTO get(@PathVariable Integer id) throws ResourceNotFoundException {
        return userService.get(id);
    }
}
