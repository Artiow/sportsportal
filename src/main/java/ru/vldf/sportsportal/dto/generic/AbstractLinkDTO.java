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

    @NotNull(groups = LinkCheck.class)
    @Min(value = 1, groups = LinkCheck.class)
    private Integer id;

    @NotNull
    @Min(value = 0, groups = LinkCheck.class)
    private Long version;


    public interface LinkCheck {

    }
}
