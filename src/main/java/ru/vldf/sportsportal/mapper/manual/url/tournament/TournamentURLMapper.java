package ru.vldf.sportsportal.mapper.manual.url.tournament;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.tournament.TournamentEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
@Named("toTournamentURL")
public class TournamentURLMapper extends AbstractURLMapper<TournamentEntity> {

    public TournamentURLMapper(@Value("${api.path.tournament.tournament}") String apiPath) {
        super(apiPath);
    }


    @Named("fromId")
    public URI toURL(Integer id) {
        return super.toURL(id);
    }

    @Named("fromEntity")
    public URI toURL(TournamentEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<TournamentEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
