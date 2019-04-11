package ru.vldf.sportsportal.dto.generic;

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
