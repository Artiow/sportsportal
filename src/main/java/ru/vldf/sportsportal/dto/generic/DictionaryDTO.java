package ru.vldf.sportsportal.dto.generic;

/**
 * @author Namednev Artem
 */
public interface DictionaryDTO extends WordbookDTO {

    String getDescription();

    void setDescription(String description);
}
