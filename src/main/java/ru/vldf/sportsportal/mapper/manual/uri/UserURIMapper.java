package ru.vldf.sportsportal.mapper.manual.uri;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
@Named("UserURIMapper")
public class UserURIMapper extends AbstractURIMapper {

    @Value("${api-path.user}")
    private String apiPath;

    @Named("toUserURI")
    public URI toUserURI(Integer identifier) {
        return toURI(apiPath, identifier);
    }
}
