package ru.vldf.sportsportal.mapper.manual.uri;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Named("PictureURIMapper")
public class PictureURIMapper extends AbstractURIMapper {

    @Value("${api-path.picture}")
    private String apiPath;

    @Named("toPictureURI")
    public URI toPictureURI(Integer identifier) {
        return toURI(apiPath, identifier);
    }
}
