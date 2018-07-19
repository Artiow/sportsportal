package ru.vldf.sportsportal.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class ResourceLocationBuilder {

    public static URI buildURI() {
        return buildComponent().toUri();
    }

    public static URI buildURI(@NotNull String prefix) {
        return buildComponent(prefix).toUri();
    }

    public static URI buildURI(@NotNull Integer identifier) {
        return buildComponent(identifier).toUri();
    }

    public static URI buildURI(@NotNull String prefix, @NotNull Integer identifier) {
        return buildComponent(prefix, identifier).toUri();
    }


    private static UriComponents buildComponent() {
        return ServletUriComponentsBuilder.fromCurrentRequest().build();
    }

    private static UriComponents buildComponent(@NotNull String prefix) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path(prefix).build();
    }

    private static UriComponents buildComponent(@NotNull Integer identifier) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/" + identifier).build();
    }

    private static UriComponents buildComponent(@NotNull String prefix, @NotNull Integer identifier) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(prefix).path("/" + identifier).build();
    }
}
