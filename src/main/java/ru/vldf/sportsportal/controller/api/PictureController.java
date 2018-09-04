package ru.vldf.sportsportal.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.vldf.sportsportal.service.PictureService;
import ru.vldf.sportsportal.service.generic.ResourceCannotCreateException;
import ru.vldf.sportsportal.service.generic.ResourceFileNotFoundException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

@RestController
@Api(tags = {"Picture"})
@RequestMapping("${api-path.common.picture}")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    private PictureService pictureService;

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    /**
     * Download picture by id.
     *
     * @param id      {@link Integer} picture identifier
     * @param request {@link HttpServletRequest} http request
     * @return picture {@link Resource}
     * @throws ResourceNotFoundException     if record not found in database
     * @throws ResourceFileNotFoundException if file not found on disk
     */
    @GetMapping("/{id}")
    @ApiOperation("получить ресурс")
    public ResponseEntity<Resource> download(@PathVariable int id, HttpServletRequest request)
            throws ResourceNotFoundException, ResourceFileNotFoundException {
        Resource resource = pictureService.get(id, null);
        MediaType contentType;

        try {
            contentType = MediaType.parseMediaType(request.getServletContext().getMimeType(resource.getFile().getAbsolutePath()));
        } catch (IOException e) {
            contentType = MediaType.APPLICATION_JSON;
            logger.info("Could Not Determine Requested File Type.");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, getContentDisposition(resource.getFilename()))
                .contentType(contentType)
                .body(resource);
    }

    /**
     * Upload picture and returns its URL.
     *
     * @param picture picture {@link MultipartFile} file
     * @return uploaded picture location
     * @throws ResourceCannotCreateException if resource cannot create
     */
    @PostMapping
    @ApiOperation("загрузить ресурс")
    public ResponseEntity<Void> upload(@RequestParam("picture") MultipartFile picture) throws ResourceCannotCreateException {
        return ResponseEntity
                .created(buildURL(pictureService.create(picture)))
                .build();
    }

    /**
     * Delete picture by id.
     *
     * @param id {@link Integer} picture identifier
     * @return no content
     * @throws ResourceNotFoundException if record not found in database
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить ресурс")
    public ResponseEntity<Void> delete(@PathVariable int id) throws ResourceNotFoundException {
        pictureService.delete(id);
        return ResponseEntity
                .noContent()
                .build();
    }


    /**
     * Build content disposition.
     *
     * @param filename {@link String} filename
     * @return {@link String} content disposition line
     */
    private String getContentDisposition(String filename) {
        return "inline; filename=\"" + filename + "\"";
    }
}
