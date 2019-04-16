package ru.vldf.sportsportal.controller.dictionary.tournament;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.TourBundleTypeDTO;
import ru.vldf.sportsportal.service.dictionary.tournament.TourBundleTypeService;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Dictionary Bundle Type"})
@RequestMapping("${api.path.tournament.dict.bundle-type}")
public class TourBundleTypeController {

    private TourBundleTypeService tourBundleTypeService;

    @Autowired
    public TourBundleTypeController(TourBundleTypeService tourBundleTypeService) {
        this.tourBundleTypeService = tourBundleTypeService;
    }


    @GetMapping("/byId/{id}")
    @ApiOperation("получить тип связки туров по идентификатору")
    public TourBundleTypeDTO get(Integer id) throws ResourceNotFoundException {
        return tourBundleTypeService.get(id);
    }

    @GetMapping("/byCode/{code}")
    @ApiOperation("получить тип связки туров по коду")
    public TourBundleTypeDTO get(String code) throws ResourceNotFoundException {
        return tourBundleTypeService.get(code);
    }

    @GetMapping("/list")
    @ApiOperation("получить страницу с типами связки туров")
    public PageDTO<TourBundleTypeDTO> getList(PageDividerDTO pageDividerDTO) {
        return tourBundleTypeService.getList(pageDividerDTO);
    }
}
