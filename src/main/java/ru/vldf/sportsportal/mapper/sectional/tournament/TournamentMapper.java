package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.*;
import ru.vldf.sportsportal.domain.sectional.tournament.TournamentEntity;
import ru.vldf.sportsportal.dto.sectional.tournament.TournamentDTO;
import ru.vldf.sportsportal.mapper.general.AbstractIdentifiedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;

import java.util.Objects;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

/**
 * @author Artem Namednev
 */
@SuppressWarnings("UnmappedTargetProperties")
@Mapper(uses = {TourBundleMapper.class, JavaTimeMapper.class})
public abstract class TournamentMapper extends AbstractIdentifiedMapper<TournamentEntity, TournamentDTO> {

    @Mappings({
            @Mapping(target = "name", source = "bundle.textLabel")
    })
    public abstract TournamentDTO toDTO(TournamentEntity entity);

    @Mappings({
            @Mapping(target = "isCompleted", nullValuePropertyMappingStrategy = IGNORE),
            @Mapping(target = "isFixed", nullValuePropertyMappingStrategy = IGNORE),
            @Mapping(target = "bundle", source = "bundle", ignore = true)
    })
    public abstract TournamentEntity toEntity(TournamentDTO dto);

    @Override
    public TournamentEntity inject(TournamentEntity acceptor, TournamentDTO donor) {

        // default value saving
        Boolean completed = donor.getIsCompleted();
        Boolean fixed = donor.getIsFixed();
        TournamentEntity mapped = toEntity(donor);
        mapped.setIsCompleted(completed);
        mapped.setIsFixed(fixed);

        merge(acceptor, mapped);
        synchronize(acceptor, donor);
        return acceptor;
    }

    @Override
    public TournamentEntity merge(TournamentEntity acceptor, TournamentEntity donor) {

        // default value mapping
        if (!Objects.isNull(donor.getIsCompleted())) {
            acceptor.setIsCompleted(donor.getIsCompleted());
        } else if (Objects.isNull(acceptor.getIsCompleted())) {
            acceptor.setIsCompleted(Boolean.FALSE);
        }
        if (!Objects.isNull(donor.getIsFixed())) {
            acceptor.setIsFixed(donor.getIsFixed());
        } else if (Objects.isNull(acceptor.getIsFixed())) {
            acceptor.setIsFixed(Boolean.FALSE);
        }

        acceptor.setStartDate(donor.getStartDate());
        acceptor.setFinishDate(donor.getFinishDate());
        return acceptor;
    }

    @AfterMapping
    public void synchronize(@MappingTarget TournamentEntity entity, TournamentDTO dto) {
        if ((entity != null) && (entity.getBundle() != null)) {
            entity.getBundle().setTextLabel(dto.getName());
            entity.getBundle().setNumericLabel(0);
        }
    }
}
