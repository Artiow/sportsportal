package ru.vldf.sportsportal.dto.sectional.tournament;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.LinkedDTO;
import ru.vldf.sportsportal.dto.general.RightsBasedDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.PictureLinkDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.UserLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class TeamDTO implements RightsBasedDTO {

    @Null(groups = FieldCheck.class)
    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
    private Long version;

    @Trimmed(groups = FieldCheck.class)
    @NotBlank(groups = FieldCheck.class)
    @Size(min = 4, max = 45, groups = FieldCheck.class)
    private String name;

    private Boolean isLocked;

    private Boolean isDisabled;

    @Null(groups = FieldCheck.class)
    private PictureLinkDTO avatar;

    @Valid
    private UserLinkDTO mainCaptain;

    @Valid
    private UserLinkDTO viceCaptain;


    public interface IdCheck extends TeamDTO.VersionCheck {

    }

    public interface CreateCheck extends TeamDTO.FieldCheck {

    }

    public interface UpdateCheck extends TeamDTO.VersionCheck, TeamDTO.FieldCheck {

    }

    private interface FieldCheck extends LinkedDTO.LinkCheck {

    }

    private interface VersionCheck {

    }
}
