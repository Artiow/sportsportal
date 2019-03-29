package ru.vldf.sportsportal.controller.dictionary.tournament;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.TourBundleStructureDTO;
import ru.vldf.sportsportal.service.dictionary.tournament.TourBundleStructureService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Dictionary Bundle Structure"})
@RequestMapping("${api.path.tournament.dict.bundle-structure}")
public class TourBundleStructureController {

    private TourBundleStructureService tourBundleStructureService;

    @Autowired
    public TourBundleStructureController(TourBundleStructureService tourBundleStructureService) {
        this.tourBundleStructureService = tourBundleStructureService;
    }


    @GetMapping("/byId/{id}")
    @ApiOperation("получить структуру связки туров по идентификатору")
    public TourBundleStructureDTO get(Integer id) throws ResourceNotFoundException {
        return tourBundleStructureService.get(id);
    }

    @GetMapping("/byCode/{code}")
    @ApiOperation("получить структуру связки туров по коду")
    public TourBundleStructureDTO get(String code) throws ResourceNotFoundException {
        return tourBundleStructureService.get(code);
    }

    @GetMapping("/list")
    @ApiOperation("получить страницу со структурами связки туров")
    public PageDTO<TourBundleStructureDTO> getList(PageDividerDTO pageDividerDTO) {
        return tourBundleStructureService.getList(pageDividerDTO);
    }
}
