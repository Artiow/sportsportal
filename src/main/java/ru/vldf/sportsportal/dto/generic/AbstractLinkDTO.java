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
public abstract class AbstractLinkDTO implements VersionedDTO {

    @NotNull
    @Min(value = 1)
    private Integer id;

    @NotNull
    @Min(value = 0)
    private Long version;
}
