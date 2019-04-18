package ru.vldf.sportsportal.dto.general;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public abstract class AbstractVersionedShortDTO extends AbstractIdentifiedShortDTO implements VersionedShortDTO {

    private Long version;
}
