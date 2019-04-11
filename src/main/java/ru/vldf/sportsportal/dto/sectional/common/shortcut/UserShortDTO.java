package ru.vldf.sportsportal.dto.sectional.common.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedShortDTO;

import java.net.URI;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class UserShortDTO extends AbstractVersionedShortDTO {

    private String email;
    private String name;
    private String surname;
    private String patronymic;
    private String address;
    private String phone;
    private URI userURL;
    private URI avatarURL;
    private List<String> roles;
}
