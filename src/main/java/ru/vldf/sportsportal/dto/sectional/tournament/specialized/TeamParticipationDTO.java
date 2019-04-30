package ru.vldf.sportsportal.dto.sectional.tournament.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.root.DataTransferObject;
import ru.vldf.sportsportal.dto.sectional.tournament.links.PlayerLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TeamLinkDTO;
import ru.vldf.sportsportal.dto.sectional.tournament.links.TournamentLinkDTO;

import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TeamParticipationDTO implements DataTransferObject {

    private TeamLinkDTO team;
    private TournamentLinkDTO tournament;
    private List<PlayerLinkDTO> players;
}
