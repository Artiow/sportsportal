package ru.vldf.sportsportal.dto.generic;

public abstract class AbstractDictionaryDTO<T extends AbstractDictionaryDTO> extends AbstractWordbookDTO {

    public abstract String getDescription();

    public abstract T setDescription(String description);
}
