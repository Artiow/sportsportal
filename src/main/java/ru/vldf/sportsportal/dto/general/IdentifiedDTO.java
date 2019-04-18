package ru.vldf.sportsportal.dto.general;

import ru.vldf.sportsportal.dto.general.root.DataTransferObject;

/**
 * @author Namednev Artem
 */
public interface IdentifiedDTO extends DataTransferObject {

    Integer getId();

    void setId(Integer id);
}
