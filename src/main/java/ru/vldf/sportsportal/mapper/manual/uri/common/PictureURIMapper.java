package ru.vldf.sportsportal.mapper.manual.uri.common;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.mapper.manual.uri.AbstractURIMapper;

import java.net.URI;

@Component
@Named("PictureURIMapper")
public class PictureURIMapper extends AbstractURIMapper {

    @Value("${api-path.common.picture}")
    private String apiPath;

    @Named("toPictureURI")
    public URI toPictureURI(Integer identifier) {
        return toURI(apiPath, identifier);
    }
}
