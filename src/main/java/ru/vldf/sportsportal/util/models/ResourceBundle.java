package ru.vldf.sportsportal.util.models;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * @author Namednev Artem
 */
public final class ResourceBundle {

    private final Resource resource;
    private final MediaType type;


    private ResourceBundle(Resource resource, MediaType type) {
        this.resource = resource;
        this.type = type;
    }


    public static ResourceBundle of(Resource resource, ServletContext context) {
        try {
            return new ResourceBundle(resource, MediaType.parseMediaType(context.getMimeType(resource.getFile().getAbsolutePath())));
        } catch (IOException e) {
            return new ResourceBundle(resource, MediaType.APPLICATION_OCTET_STREAM);
        }
    }

    private static String disposition(String filename) {
        return "inline; filename=\"" + filename + "\"";
    }


    public Resource getBody() {
        return resource;
    }

    public MediaType getContentType() {
        return type;
    }

    public String getContentDisposition() {
        return disposition(resource.getFilename());
    }
}
