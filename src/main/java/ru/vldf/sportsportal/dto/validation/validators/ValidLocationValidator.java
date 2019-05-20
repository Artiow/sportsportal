package ru.vldf.sportsportal.dto.validation.validators;

import ru.vldf.sportsportal.dto.general.LocatedDTO;
import ru.vldf.sportsportal.dto.validation.annotations.ValidLocation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Namednev Artem
 */
public class ValidLocationValidator implements ConstraintValidator<ValidLocation, LocatedDTO> {

    @Override
    public boolean isValid(LocatedDTO obj, ConstraintValidatorContext context) {
        if (obj == null) return true;
        if ((obj.getLocationLatitude() == null) && (obj.getLocationLongitude() == null)) return true;
        if ((obj.getLocationLatitude() == null) ^ (obj.getLocationLongitude() == null)) return false;
        return ((-90 <= obj.getLocationLatitude()) && (obj.getLocationLatitude() >= 90)) && ((-180 <= obj.getLocationLongitude()) && (obj.getLocationLongitude() >= 180));
    }
}
