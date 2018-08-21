package ru.vldf.sportsportal.mapper.manual.url;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.util.ResourceLocationBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractURLMapper<T extends AbstractIdentifiedEntity> {

    protected abstract String getApiPath();


    public URI toURL(Integer id) {
        if (id != null) {
            return ResourceLocationBuilder.buildURL(getApiPath(), id);
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
            Collection<URI> uriCollection = new ArrayList<>(entityCollection.size());
            for (T entity : entityCollection) {
                uriCollection.add(toURL(entity));
            }
            return uriCollection;
        } else {
            return null;
        }
    }
}
