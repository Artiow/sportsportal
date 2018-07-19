package ru.vldf.sportsportal.mapper.manual.uri;

import ru.vldf.sportsportal.util.ResourceLocationBuilder;

import java.net.URI;

public abstract class AbstractURIMapper {

    protected URI toURI(String apiPath, Integer identifier) {
        return ResourceLocationBuilder.buildURI(apiPath, identifier);
    }
}
