package ru.vldf.sportsportal.dto.sectional.tournament.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedLinkDTO;

import java.net.URI;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlayerLinkDTO extends AbstractVersionedLinkDTO {

    private String name;
    private String surname;
    private URI playerURL;
    private URI avatarURL;
}
