package ru.vldf.sportsportal.mapper.manual.uri;

import ru.vldf.sportsportal.domain.generic.AbstractIdentifiedEntity;
import ru.vldf.sportsportal.util.ResourceLocationBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractURIMapper<T extends AbstractIdentifiedEntity> {

    protected abstract String getApiPath();


    public URI toURI(Integer id) {
        if (id != null) {
            return ResourceLocationBuilder.buildURI(getApiPath(), id);
        } else {
            return null;
        }
    }

    public URI toURI(T entity) {
        if (entity != null) {
            return toURI(entity.getId());
        } else {
            return null;
        }
    }

    public Collection<URI> toURI(Collection<T> entityCollection) {
        if (entityCollection != null) {
            Collection<URI> uriCollection = new ArrayList<>(entityCollection.size());
            for (T entity : entityCollection) {
                uriCollection.add(toURI(entity));
            }
            return uriCollection;
        } else {
            return null;
        }
    }
}
