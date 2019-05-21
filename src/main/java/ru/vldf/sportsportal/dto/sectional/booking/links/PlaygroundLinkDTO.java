package ru.vldf.sportsportal.dto.sectional.booking.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedLinkDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundLinkDTO extends AbstractVersionedLinkDTO {

    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private BigDecimal price;
    private URI playgroundURL;
    private URI avatarURL;
}
