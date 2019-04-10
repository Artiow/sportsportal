package ru.vldf.sportsportal.dto.sectional.lease.specialized;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.AbstractVersionedLinkDTO;

import java.math.BigDecimal;
import java.net.URI;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class OrderLinkDTO extends AbstractVersionedLinkDTO {

    private URI orderURL;
    private URI customerURL;
    private BigDecimal price;
}
