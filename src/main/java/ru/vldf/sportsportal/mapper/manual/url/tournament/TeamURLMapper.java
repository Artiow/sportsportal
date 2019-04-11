package ru.vldf.sportsportal.mapper.manual.url.tournament;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.domain.sectional.tournament.TeamEntity;
import ru.vldf.sportsportal.mapper.manual.url.AbstractURLMapper;

import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Component
@Named("toTeamURL")
public class TeamURLMapper extends AbstractURLMapper<TeamEntity> {

    public TeamURLMapper(@Value("${api.path.tournament.team}") String apiPath) {
        super(apiPath);
    }


    @Named("fromId")
    public URI toURL(Integer id) {
        return super.toURL(id);
    }

    @Named("fromEntity")
    public URI toURL(TeamEntity entity) {
        return super.toURL(entity);
    }

    @Named("fromCollection")
    public Collection<URI> toURL(Collection<TeamEntity> entityCollection) {
        return super.toURL(entityCollection);
    }
}
