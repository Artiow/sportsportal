package ru.vldf.sportsportal.mapper.manual.url.lease;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.lease.PlaygroundEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
@Named("toPlaygroundURL")
public class PlaygroundURLMapper extends AbstractURLMapper<PlaygroundEntity> {

    public PlaygroundURLMapper(@Value("${api.path.lease.playground}") String apiPath) {
        super(apiPath);
    }


    @Named("fromId")
    public URI toURL(Integer id) {
        return super.toURL(id);
    }

    @Named("fromEntity")
    public URI toURL(PlaygroundEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<PlaygroundEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
