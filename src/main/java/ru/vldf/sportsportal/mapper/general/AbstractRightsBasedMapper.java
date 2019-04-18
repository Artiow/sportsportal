package ru.vldf.sportsportal.mapper.general;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.vldf.sportsportal.domain.general.AbstractRightsBasedEntity;
import ru.vldf.sportsportal.dto.general.RightsBasedDTO;

import javax.persistence.OptimisticLockException;
import java.util.Objects;

/**
 * @author Namednev Artem
 */
@MapperConfig
public abstract class AbstractRightsBasedMapper<E extends AbstractRightsBasedEntity, D extends RightsBasedDTO> extends AbstractVersionedMapper<E, D> {

    @Mappings({
            @Mapping(target = "isLocked", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE),
            @Mapping(target = "isDisabled", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    })
    public abstract E toEntity(D dto);

    @Override
    @SuppressWarnings("UnmappedTargetProperties")
    public E inject(E acceptor, D donor) {
        boolean locked = donor.getIsLocked();
        boolean disabled = donor.getIsDisabled();
        E mapped = toEntity(donor);
        mapped.setIsLocked(locked);
        mapped.setIsDisabled(disabled);
        return merge(acceptor, mapped);
    }

    @Override
    @SuppressWarnings("UnmappedTargetProperties")
    public E merge(E acceptor, E donor) throws OptimisticLockException {
        super.merge(acceptor, donor);
        if (!Objects.isNull(donor.getIsLocked())) {
            acceptor.setIsLocked(donor.getIsLocked());
        } else if (Objects.isNull(acceptor.getIsLocked())) {
            acceptor.setIsLocked(Boolean.FALSE);
        }
        if (!Objects.isNull(donor.getIsDisabled())) {
            acceptor.setIsDisabled(donor.getIsDisabled());
        } else if (Objects.isNull(acceptor.getIsDisabled())) {
            acceptor.setIsDisabled(Boolean.TRUE);
        }
        return acceptor;
    }
}
