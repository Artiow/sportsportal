package ru.vldf.sportsportal.dto.generic;

public abstract class AbstractIdentifiedDTO<T extends AbstractIdentifiedDTO> implements DataTransferObject {

    public abstract Integer getId();

    public abstract T setId(Integer id);
}
