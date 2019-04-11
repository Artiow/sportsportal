package ru.vldf.sportsportal.dto.sectional.lease.links;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedLinkDTO;

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
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private BigDecimal price;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;
}
