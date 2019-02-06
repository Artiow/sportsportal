package ru.vldf.sportsportal.dto.sectional.lease.shortcut;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.generic.VersionedDTO;
import ru.vldf.sportsportal.dto.generic.WorkTimeDTO;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PlaygroundShortDTO implements VersionedDTO, WorkTimeDTO {

    private Integer id;
    private Long version;
    private String name;
    private String address;
    private String phone;
    private Integer rate;
    private LocalTime opening;
    private LocalTime closing;
    private Boolean halfHourAvailable;
    private Boolean fullHourRequired;
    private BigDecimal price;
    private List<String> specializations;
    private List<String> capabilities;
    private URI playgroundURL;
    private List<URI> ownersURLs;
    private List<URI> photoURLs;
}
