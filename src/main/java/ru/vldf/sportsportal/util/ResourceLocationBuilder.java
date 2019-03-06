package ru.vldf.sportsportal.util;

import org.springframework.util.Assert;

import java.net.URI;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

public class ResourceLocationBuilder {

    public static URI buildURL() {
        return fromCurrentRequest().replacePath(null).build().toUri();
    }

    public static URI buildURL(Integer identifier) {
        Assert.notNull(identifier, "id must not be null");
        return fromCurrentRequest().path("/" + identifier).build().toUri();
    }

    public static URI buildURL(String path, Integer identifier) {
        Assert.hasLength(path, "path must not be blank");
        Assert.notNull(identifier, "id must not be null");
        return fromCurrentRequest().replacePath(path).path("/" + identifier).build().toUri();
    }
}
