package ru.vldf.sportsportal.dto.sectional.common.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractIdentifiedLinkDTO;

import java.net.URI;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PictureLinkDTO extends AbstractIdentifiedLinkDTO {

    private URI pictureURL;
}
