package ru.vldf.sportsportal.dto.generic;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public abstract class AbstractIdentifiedLinkDTO implements LinkedDTO, IdentifiedDTO {

    @NotNull(groups = LinkCheck.class)
    @Min(value = 1, groups = LinkCheck.class)
    private Integer id;
}
