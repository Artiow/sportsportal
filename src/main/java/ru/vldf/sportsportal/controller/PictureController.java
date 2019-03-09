package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.service.PictureService;
import ru.vldf.sportsportal.service.generic.*;
import ru.vldf.sportsportal.util.ResourceBundle;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

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
     * Download picture by id.
     *
     * @param id   the picture identifier.
     * @param size the picture size code.
     * @return picture resource.
     * @throws ResourceNotFoundException     if record not found in database.
     * @throws ResourceFileNotFoundException if file not found on disk.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить ресурс")
    public ResponseEntity<Resource> download(
            @PathVariable int id, @RequestParam(name = "size", required = false) String size
    ) throws ResourceNotFoundException, ResourceFileNotFoundException {
        ResourceBundle resource = pictureService.get(id, size);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, resource.getContentDisposition()).contentType(resource.getContentType()).body(resource.getBody());
    }

    /**
     * Upload picture and returns its URL.
     *
     * @param picture the picture file.
     * @return uploaded picture location.
     * @throws UnauthorizedAccessException   if authorization is missing.
     * @throws ResourceCannotCreateException if resource cannot be create.
     */
    @PostMapping
    @ApiOperation("загрузить ресурс")
    public ResponseEntity<Void> upload(@RequestParam("picture") MultipartFile picture) throws UnauthorizedAccessException, ResourceCannotCreateException {
        return ResponseEntity.created(buildURL(pictureService.create(picture))).build();
    }

    /**
     * Delete picture by id.
     *
     * @param id the picture identifier.
     * @return no content.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this picture.
     * @throws ResourceNotFoundException   if picture not found in database.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить ресурс")
    public ResponseEntity<Void> delete(@PathVariable int id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        pictureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
