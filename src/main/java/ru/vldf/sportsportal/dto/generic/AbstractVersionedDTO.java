package ru.vldf.sportsportal.dto.generic;

public abstract class AbstractVersionedDTO<T extends AbstractIdentifiedDTO> extends AbstractIdentifiedDTO {

    public abstract Long getVersion();

    public abstract T setVersion(Long version);
}
