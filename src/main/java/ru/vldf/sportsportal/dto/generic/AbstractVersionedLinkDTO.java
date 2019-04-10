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
public abstract class AbstractVersionedLinkDTO extends AbstractIdentifiedLinkDTO implements VersionedDTO {

    @NotNull(groups = LinkCheck.class)
    @Min(value = 0, groups = LinkCheck.class)
    private Long version;

    public interface LinkCheck extends AbstractIdentifiedLinkDTO.LinkCheck {

    }
}
