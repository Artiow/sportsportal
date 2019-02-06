package ru.vldf.sportsportal.dto.generic;

/**
 * @author Namednev Artem
 */
public interface IdentifiedDTO extends DataTransferObject {

    Integer getId();

    void setId(Integer id);
}
