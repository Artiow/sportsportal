package ru.vldf.sportsportal.controller.dictionary.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.common.PictureSizeDTO;
import ru.vldf.sportsportal.service.dictionary.common.PictureSizeService;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Dictionary Picture Size"})
@RequestMapping("${api.path.common.dict.picturesize}")
public class PictureSizeController {

    private PictureSizeService pictureSizeService;

    @Autowired
    public PictureSizeController(PictureSizeService pictureSizeService) {
        this.pictureSizeService = pictureSizeService;
    }


    @GetMapping("/byId/{id}")
    @ApiOperation("получить размер изображения по идентификатору")
    public PictureSizeDTO get(Integer id) throws ResourceNotFoundException {
        return pictureSizeService.get(id);
    }

    @GetMapping("/byCode/{code}")
    @ApiOperation("получить размер изображения по коду")
    public PictureSizeDTO get(String code) throws ResourceNotFoundException {
        return pictureSizeService.get(code);
    }

    @GetMapping("/list")
    @ApiOperation("получить страницу с размерами изображений")
    public PageDTO<PictureSizeDTO> getList(PageDividerDTO pageDividerDTO) {
        return pictureSizeService.getList(pageDividerDTO);
    }
}
