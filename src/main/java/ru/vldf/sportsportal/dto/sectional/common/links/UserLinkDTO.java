package ru.vldf.sportsportal.dto.sectional.common.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedLinkDTO;

import java.net.URI;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class UserLinkDTO extends AbstractVersionedLinkDTO {

    private String email;
    private String name;
    private String surname;
    private String phone;
    private URI userURL;
    private URI avatarURL;
}
