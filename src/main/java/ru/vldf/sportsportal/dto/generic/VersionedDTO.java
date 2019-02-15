package ru.vldf.sportsportal.dto.generic;

/**
 * @author Namednev Artem
 */
public interface VersionedDTO extends IdentifiedDTO {

    Long getVersion();

    void setVersion(Long version);
}
