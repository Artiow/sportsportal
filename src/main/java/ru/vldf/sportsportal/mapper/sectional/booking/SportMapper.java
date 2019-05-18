package ru.vldf.sportsportal.mapper.sectional.booking;

import org.mapstruct.Mapper;
import ru.vldf.sportsportal.domain.sectional.booking.SportEntity;
import ru.vldf.sportsportal.dto.sectional.booking.SportDTO;
import ru.vldf.sportsportal.mapper.general.AbstractDictionaryMapper;

/**
 * @author Namednev Artem
 */
@Mapper
public abstract class SportMapper extends AbstractDictionaryMapper<SportEntity, SportDTO> {

}
