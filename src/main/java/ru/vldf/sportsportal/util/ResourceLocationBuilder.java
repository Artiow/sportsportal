package ru.vldf.sportsportal.util;

import org.springframework.util.Assert;

import java.net.URI;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

public final class ResourceLocationBuilder {

    public static URI buildURL() {
        return fromCurrentRequestUri().replacePath(null).build().toUri();
    }

    public static URI buildURL(String path) {
        Assert.hasLength(path, "path must not be blank");
        return fromCurrentRequestUri().replacePath(path).build().toUri();
    }

    public static URI buildURL(Integer identifier) {
        Assert.notNull(identifier, "id must not be null");
        return fromCurrentRequestUri().path("/" + identifier).build().toUri();
    }

    public static URI buildURL(String path, Integer identifier) {
        Assert.hasLength(path, "path must not be blank");
        Assert.notNull(identifier, "id must not be null");
        return fromCurrentRequestUri().replacePath(path).path("/" + identifier).build().toUri();
    }
}
