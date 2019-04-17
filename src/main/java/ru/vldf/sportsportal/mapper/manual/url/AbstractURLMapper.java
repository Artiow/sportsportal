package ru.vldf.sportsportal.mapper.manual.url;

import io.jsonwebtoken.lang.Assert;
import ru.vldf.sportsportal.domain.general.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.util.ResourceLocationBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Namednev Artem
 */
public abstract class AbstractURLMapper<T extends AbstractIdentifiedEntity> {

    private final String apiPath;


    protected AbstractURLMapper(String apiPath) {
        Assert.hasText(apiPath);
        this.apiPath = apiPath;
    }


    public URI toURL(Integer id) {
        if (id != null) {
            return ResourceLocationBuilder.buildURL(apiPath, id);
        } else {
            return null;
        }
    }

    public URI toURL(T entity) {
        if (entity != null) {
            return toURL(entity.getId());
        } else {
            return null;
        }
    }

    public Collection<URI> toURL(Collection<T> entityCollection) {
        if (entityCollection != null) {
            return entityCollection.stream().map(this::toURL).collect(Collectors.toList());
        } else {
            return null;
        }
    }
}
