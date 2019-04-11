package ru.vldf.sportsportal.dto.generic;

/**
 * @author Namednev Artem
 */
public interface VersionedLinkDTO extends VersionedDTO, LinkedDTO {

    Long getVersion();

    void setVersion(Long version);
}
