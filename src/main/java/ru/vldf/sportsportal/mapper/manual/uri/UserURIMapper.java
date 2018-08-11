package ru.vldf.sportsportal.mapper.manual.uri;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class UserURIMapper extends AbstractURIMapper {

    @Value("${api-path.user}")
    private String apiPath;

    public URI toURI(Integer identifier) {
        return toURI(apiPath, identifier);
    }
}
