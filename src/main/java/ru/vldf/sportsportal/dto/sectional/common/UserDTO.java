package ru.vldf.sportsportal.dto.sectional.common;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.VersionedDTO;
import ru.vldf.sportsportal.dto.sectional.common.links.PictureLinkDTO;
import ru.vldf.sportsportal.dto.validation.annotations.Email;
import ru.vldf.sportsportal.dto.validation.annotations.Phone;
import ru.vldf.sportsportal.dto.validation.annotations.Trimmed;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class UserDTO implements VersionedDTO {

    @NotNull(groups = IdCheck.class)
    @Min(value = 1, groups = IdCheck.class)
    private Integer id;

    @NotNull(groups = VersionCheck.class)
    @Min(value = 0, groups = VersionCheck.class)
    private Long version;

    @Trimmed(groups = FieldCheck.class)
    @NotBlank(groups = FieldCheck.class)
    @Size(min = 5, max = 254, groups = FieldCheck.class)
    @Email(groups = FieldCheck.class)
    private String email;

    @NotBlank(groups = FieldCheck.class)
    @Size(min = 4, max = 50, groups = FieldCheck.class)
    private String password;

    @Trimmed(groups = FieldCheck.class)
    @NotBlank(groups = FieldCheck.class)
    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String name;

    @Trimmed(groups = FieldCheck.class)
    @NotBlank(groups = FieldCheck.class)
    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String surname;

    @Trimmed(groups = FieldCheck.class)
    @Size(min = 2, max = 45, groups = FieldCheck.class)
    private String patronymic;

    @Trimmed(groups = FieldCheck.class)
    @Size(min = 5, max = 254, groups = FieldCheck.class)
    private String address;

    @Trimmed(groups = FieldCheck.class)
    @Phone(groups = FieldCheck.class)
    private String phone;

    @Null(groups = FieldCheck.class)
    private PictureLinkDTO avatar;

    @Null(groups = FieldCheck.class)
    private List<RoleDTO> roles;


    public interface IdCheck extends VersionCheck {

    }

    public interface CreateCheck extends FieldCheck {

    }

    public interface UpdateCheck extends VersionCheck, FieldCheck {

    }

    private interface VersionCheck {

    }

    private interface FieldCheck {

    }
}
