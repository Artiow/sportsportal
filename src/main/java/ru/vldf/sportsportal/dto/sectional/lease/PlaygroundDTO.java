package ru.vldf.sportsportal.dto.sectional.lease;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.VersionedDTO;
import ru.vldf.sportsportal.dto.generic.WorkTimeDTO;
import ru.vldf.sportsportal.dto.sectional.common.PictureDTO;
import ru.vldf.sportsportal.dto.sectional.common.specialized.UserLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Phone;
import ru.vldf.sportsportal.dto.validation.annotations.ValidWorkTime;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@ValidWorkTime(groups = {PlaygroundDTO.CreateCheck.class, PlaygroundDTO.UpdateCheck.class})
public class PlaygroundDTO implements VersionedDTO, WorkTimeDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
    private Long version;

    @NotBlank(groups = FieldCheck.class)
    @Size(min = 4, max = 45, groups = FieldCheck.class)
    private String name;

    @NotBlank(groups = FieldCheck.class)
    @Size(min = 4, max = 90, groups = FieldCheck.class)
    private String address;

    @NotBlank(groups = FieldCheck.class)
    @Phone(groups = FieldCheck.class)
    private String phone;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 0, groups = FieldCheck.class)
    @Max(value = 10, groups = FieldCheck.class)
    private Integer rate;

    @NotNull(groups = FieldCheck.class)
    private LocalTime opening;

    @NotNull(groups = FieldCheck.class)
    private LocalTime closing;

    @NotNull(groups = FieldCheck.class)
    private Boolean halfHourAvailable;

    @NotNull(groups = FieldCheck.class)
    private Boolean fullHourRequired;

    @NotNull(groups = FieldCheck.class)
    @Min(value = 0, groups = FieldCheck.class)
    @Digits(integer = 6, fraction = 2, groups = FieldCheck.class)
    private BigDecimal price;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<SportDTO> specializations;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<FeatureDTO> capabilities;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<PictureDTO> photos;

    @Valid
    @NotNull(groups = FieldCheck.class)
    private List<UserLinkDTO> owners;


    public interface IdCheck extends VersionCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends VersionCheck, FieldCheck {

    }

    private interface VersionCheck {

    }

    private interface FieldCheck extends
            SportDTO.IdCheck, FeatureDTO.IdCheck, PictureDTO.IdCheck {

    }
}
