package ru.vldf.sportsportal.mapper.manual.url.tournament;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.tournament.PlayerEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
@Named("toPlayerURL")
public class PlayerURLMapper extends AbstractURLMapper<PlayerEntity> {

    public PlayerURLMapper(@Value("${api.path.tournament.player}") String apiPath) {
        super(apiPath);
    }


    @Named("fromId")
    public URI toURL(Integer id) {
        return super.toURL(id);
    }

    @Named("fromEntity")
    public URI toURL(PlayerEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<PlayerEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
