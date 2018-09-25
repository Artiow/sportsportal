package ru.vldf.sportsportal.controller.dictionary.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.common.RoleDTO;
import ru.vldf.sportsportal.service.dictionary.common.RoleService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

@RestController
@Api(tags = {"Dictionary Role"})
@RequestMapping("${api.path.common.dict.role}")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }


    @GetMapping("/byId/{id}")
    @ApiOperation("получить роль по идентификатору")
    public RoleDTO get(Integer id) throws ResourceNotFoundException {
        return roleService.get(id);
    }

    @GetMapping("/byCode/{code}")
    @ApiOperation("получить роль по коду")
    public RoleDTO get(String code) throws ResourceNotFoundException {
        return roleService.get(code);
    }

    @GetMapping("/list")
    @ApiOperation("получить страницу с ролями")
    public PageDTO<RoleDTO> get(PageDividerDTO pageDividerDTO) {
        return roleService.get(pageDividerDTO);
    }
}
