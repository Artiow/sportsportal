package ru.vldf.sportsportal.mapper.sectional.tournament;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    @Mapping(target = "name", source = "bundle.textLabel")
    public abstract TournamentDTO toDTO(TournamentEntity entity);

    @Mapping(target = "isCompleted", nullValuePropertyMappingStrategy = IGNORE)
    public abstract TournamentEntity toEntity(TournamentDTO dto);

    @Override
    public TournamentEntity inject(TournamentEntity acceptor, TournamentDTO donor) {
        // default value saving
        Boolean completed = donor.getIsCompleted();
        TournamentEntity mapped = toEntity(donor);
        mapped.setIsCompleted(completed);

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
