package ru.vldf.sportsportal.dto.general;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public abstract class AbstractIdentifiedShortDTO implements IdentifiedShortDTO {

    private Integer id;
}
