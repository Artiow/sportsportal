package ru.vldf.sportsportal.dto.generic;

public abstract class AbstractWordbookDTO<T extends AbstractWordbookDTO> extends AbstractIdentifiedDTO {

    public abstract String getCode();

    public abstract T setCode(String code);

    public abstract String getName();

    public abstract T setName(String name);
}
