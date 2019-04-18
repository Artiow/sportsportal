package ru.vldf.sportsportal.dto.general;

/**
 * @author Namednev Artem
 */
public interface RightsBasedDTO extends VersionedDTO {

    Boolean getIsLocked();

    void setIsLocked(Boolean isLocked);

    Boolean getIsDisabled();

    void setIsDisabled(Boolean isDisabled);
}
