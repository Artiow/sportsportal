package ru.vldf.sportsportal.dto.sectional.booking.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.general.AbstractVersionedShortDTO;
import ru.vldf.sportsportal.dto.general.LocatedDTO;
import ru.vldf.sportsportal.dto.general.WorkTimeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundShortDTO extends AbstractVersionedShortDTO implements WorkTimeDTO, LocatedDTO {

    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private LocalTime opening;
    private LocalTime closing;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private BigDecimal price;
    private Double locationLatitude;
    private Double locationLongitude;
    private Boolean isTested;
    private Boolean isFreed;
    private List<String> specializations;
    private List<String> capabilities;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;
}
