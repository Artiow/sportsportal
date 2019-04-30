package ru.vldf.sportsportal.dto.sectional.tournament.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TournamentLinkDTO;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TeamParticipationDTO implements DataTransferObject {

    private TournamentLinkDTO tournament;
    private TeamLinkDTO team;
}
