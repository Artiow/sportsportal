package ru.vldf.sportsportal.dto.sectional.tournament.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedShortDTO;

import java.net.URI;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TeamShortDTO extends AbstractVersionedShortDTO {

    private String name;
    private Boolean isLocked;
    private Boolean isDisabled;
    private URI teamURL;
    private URI avatarURL;
    private URI mainCaptainURL;
    private URI viceCaptainURL;
}
