package ru.vldf.sportsportal.controller.dictionary.booking;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.booking.SportDTO;
import ru.vldf.sportsportal.service.dictionary.booking.SportService;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Dictionary Sport"})
@RequestMapping("${api.path.booking.dict.sport}")
public class SportController {

    private SportService sportService;

    @Autowired
    public SportController(SportService sportService) {
        this.sportService = sportService;
    }


    @GetMapping("/byId/{id}")
    @ApiOperation("получить вид спорта по идентификатору")
    public SportDTO get(Integer id) throws ResourceNotFoundException {
        return sportService.get(id);
    }

    @GetMapping("/byCode/{code}")
    @ApiOperation("получить вид спорта по коду")
    public SportDTO get(String code) throws ResourceNotFoundException {
        return sportService.get(code);
    }

    @GetMapping("/list")
    @ApiOperation("получить страницу с видами спорта")
    public PageDTO<SportDTO> getList(PageDividerDTO pageDividerDTO) {
        return sportService.getList(pageDividerDTO);
    }
}
