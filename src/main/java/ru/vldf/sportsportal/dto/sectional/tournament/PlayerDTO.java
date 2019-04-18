package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.LinkedDTO;
import ru.vldf.sportsportal.dto.general.RightsBasedDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.PictureLinkDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.UserLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Past;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlayerDTO implements RightsBasedDTO {

    @Null(groups = FieldCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
    private Long version;

    @NotBlank(groups = FieldCheck.class)
    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String name;

    @NotBlank(groups = FieldCheck.class)
    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String surname;

    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String patronymic;

    @Past
    @NotNull(groups = FieldCheck.class)
    private LocalDate birthdate;

    private Boolean isLocked;

    private Boolean isDisabled;

    @Null(groups = FieldCheck.class)
    private UserLinkDTO user;

    @Null(groups = FieldCheck.class)
    private PictureLinkDTO avatar;


    public interface IdCheck extends PlayerDTO.VersionCheck {

    }

    public interface CreateCheck extends PlayerDTO.FieldCheck {

    }

    public interface UpdateCheck extends PlayerDTO.VersionCheck, PlayerDTO.FieldCheck {

    }

    private interface FieldCheck extends LinkedDTO.LinkCheck {

    }

    private interface VersionCheck {

    }
}
