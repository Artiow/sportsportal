package ru.vldf.sportsportal.dto.generic;

/**
 * @author Namednev Artem
 */
public interface WordbookDTO extends IdentifiedDTO {

    String getCode();

    void setCode(String code);

    String getName();

    void setName(String name);
}
