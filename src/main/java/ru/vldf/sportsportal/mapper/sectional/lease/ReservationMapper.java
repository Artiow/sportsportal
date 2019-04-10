package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.sectional.lease.specialized.ReservationResumeDTO;
import ru.vldf.sportsportal.mapper.generic.ModelBidirectionalMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Namednev Artem
 */
@Mapper(componentModel = "spring", uses = {JavaTimeMapper.class, PlaygroundMapper.class})
public abstract class ReservationMapper implements ModelBidirectionalMapper<ReservationEntity, ReservationResumeDTO.Item> {

    @Mapping(target = "datetime", source = "pk.datetime")
    public abstract ReservationResumeDTO.Item toDTO(ReservationEntity entity);

    @Mapping(target = "pk.datetime", source = "datetime")
    public abstract ReservationEntity toEntity(ReservationResumeDTO.Item dto);

    @Mapping(target = "playground", source = "pk.playground")
    public abstract ReservationResumeDTO toResume(ReservationEntity entity);

    public ReservationResumeDTO toResume(Collection<ReservationEntity> entityCollection) {
        if ((entityCollection == null) || entityCollection.isEmpty()) {
            return null;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ReservationEntity entity : entityCollection) {
            totalPrice = totalPrice.add(entity.getPrice());
        }

        ReservationResumeDTO resume = toResume(entityCollection.iterator().next());
        resume.setReservations(toDTO(entityCollection));
        resume.setTotalPrice(totalPrice);
        return resume;
    }

    public List<ReservationResumeDTO> toResumeList(Collection<ReservationEntity> entityCollection) {
        if (entityCollection == null) {
            return null;
        }

        Map<Integer, Collection<ReservationEntity>> map = new HashMap<>(1);
        for (ReservationEntity entity : entityCollection) {
            Integer key = entity.getPlayground().getId();
            Collection<ReservationEntity> value = map.getOrDefault(key, new ArrayList<>());
            if (value.isEmpty()) map.put(key, value);
            value.add(entity);
        }

        Set<Map.Entry<Integer, Collection<ReservationEntity>>> entrySet = map.entrySet();
        List<ReservationResumeDTO> list = new ArrayList<>(entrySet.size());
        for (Map.Entry<Integer, Collection<ReservationEntity>> entry : entrySet) {
            list.add(toResume(entry.getValue()));
        }

        return list;
    }
}
