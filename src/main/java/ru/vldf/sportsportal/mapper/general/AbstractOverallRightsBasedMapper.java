package ru.vldf.sportsportal.mapper.general;

import org.mapstruct.MapperConfig;
import ru.vldf.sportsportal.domain.general.AbstractRightsBasedEntity;
import ru.vldf.sportsportal.dto.general.RightsBasedDTO;
import ru.vldf.sportsportal.dto.general.VersionedLinkDTO;
import ru.vldf.sportsportal.dto.general.VersionedShortDTO;

/**
 * @author Namednev Artem
 */
@MapperConfig
public abstract class AbstractOverallRightsBasedMapper<E extends AbstractRightsBasedEntity, D extends RightsBasedDTO, S extends VersionedShortDTO, L extends VersionedLinkDTO>
        extends AbstractRightsBasedMapper<E, D> implements ShortMapper<E, S>, LinkMapper<E, L> {

}
