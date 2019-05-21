package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.service.PictureService;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.util.models.ResourceBundle;

/**
 * @author Namednev Artem
 */
@Slf4j
@RestController
@Api(tags = {"Picture"})
@RequestMapping("${api.path.common.picture}")
public class PictureController {

    private final PictureService pictureService;


    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    /**
     * Download picture by picture identifier.
     *
     * @param id   the picture identifier.
     * @param size the picture size code.
     * @return picture resource.
     * @throws ResourceNotFoundException if picture not found.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить картинку")
    public ResponseEntity<Resource> download(@PathVariable int id, @RequestParam(name = "size", required = false) String size) throws ResourceNotFoundException {
        ResourceBundle resource = pictureService.download(id, size);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, resource.getContentDisposition()).contentType(resource.getContentType()).body(resource.getBody());
    }
}
