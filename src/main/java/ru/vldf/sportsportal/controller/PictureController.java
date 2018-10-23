package ru.vldf.sportsportal.controller;

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
import ru.vldf.sportsportal.service.generic.*;

import javax.servlet.ServletContext;
import java.io.IOException;

import static ru.vldf.sportsportal.util.ResourceLocationBuilder.buildURL;

@RestController
@Api(tags = {"Picture"})
@RequestMapping("${api.path.common.picture}")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    private ServletContext context;

    private PictureService pictureService;

    @Autowired
    public void setContext(ServletContext context) {
        this.context = context;
    }

    @Autowired
    public void setPictureService(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    /**
     * Download picture by id.
     *
     * @param id picture identifier
     * @return picture {@link Resource}
     * @throws ResourceNotFoundException     if record not found in database
     * @throws ResourceFileNotFoundException if file not found on disk
     */
    @GetMapping("/{id}")
    @ApiOperation("получить ресурс")
    public ResponseEntity<Resource> download(@PathVariable int id, @RequestParam(name = "size", required = false) String size)
            throws ResourceNotFoundException, ResourceFileNotFoundException {
        Resource resource = pictureService.get(id, size);
        MediaType contentType;
        try {
            contentType = MediaType.parseMediaType(context.getMimeType(resource.getFile().getAbsolutePath()));
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
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ResourceCannotCreateException if resource cannot create
     */
    @PostMapping
    @ApiOperation("загрузить ресурс")
    public ResponseEntity<Void> upload(@RequestParam("picture") MultipartFile picture) throws UnauthorizedAccessException, ResourceCannotCreateException {
        return ResponseEntity
                .created(buildURL(pictureService.create(picture)))
                .build();
    }

    /**
     * Delete picture by id.
     *
     * @param id picture identifier
     * @return no content
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this picture
     * @throws ResourceNotFoundException   if picture not found in database
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить ресурс")
    public ResponseEntity<Void> delete(@PathVariable int id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
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
