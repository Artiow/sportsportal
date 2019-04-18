package ru.vldf.sportsportal.dto.sectional.tournament.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedShortDTO;

import java.net.URI;
import java.time.LocalDate;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlayerShortDTO extends AbstractVersionedShortDTO {

    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthdate;
    private Boolean isLocked;
    private Boolean isDisabled;
    private URI playerURL;
    private URI avatarURL;
}
