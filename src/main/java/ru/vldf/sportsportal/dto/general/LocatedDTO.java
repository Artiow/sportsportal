package ru.vldf.sportsportal.dto.general;

/**
 * @author Namednev Artem
 */
public interface LocatedDTO {

    Double getLocationLatitude();

    void setLocationLatitude(Double locationLatitude);

    Double getLocationLongitude();

    void setLocationLongitude(Double locationLongitude);
}
