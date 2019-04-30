package ru.vldf.sportsportal.dto.sectional.tournament.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractIdentifiedLinkDTO;

import java.net.URI;
import java.time.LocalDate;

/**
 * @author Artem Namednev
 */
@Getter
@Setter
public class TournamentLinkDTO extends AbstractIdentifiedLinkDTO {

    private String name;
    private LocalDate startDate;
    private LocalDate finishDate;
    private URI tournamentURL;
}
