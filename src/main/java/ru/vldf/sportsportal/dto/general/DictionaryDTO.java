package ru.vldf.sportsportal.dto.general;

/**
 * @author Namednev Artem
 */
public interface DictionaryDTO extends WordbookDTO {

    String getDescription();

    void setDescription(String description);
}
