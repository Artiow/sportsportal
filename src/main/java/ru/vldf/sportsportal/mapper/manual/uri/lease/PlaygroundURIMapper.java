package ru.vldf.sportsportal.mapper.manual.uri.lease;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.mapper.manual.uri.AbstractURIMapper;

import java.net.URI;
import java.util.Collection;

@Component
@Named("toPlaygroundURI")
public class PlaygroundURIMapper extends AbstractURIMapper<PlaygroundEntity> {

    @Value("${api-path.lease.playground}")
    private String apiPath;

    @Override
    protected String getApiPath() {
        return apiPath;
    }


    @Named("fromId")
    public URI toURI(Integer id) {
        return super.toURI(id);
    }

    @Named("fromEntity")
    public URI toURI(PlaygroundEntity entity) {
        return super.toURI(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURI(Collection<PlaygroundEntity> entityCollection) {
        return super.toURI(entityCollection);
    }
}
