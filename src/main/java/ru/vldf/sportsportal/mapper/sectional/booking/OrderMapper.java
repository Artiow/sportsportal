package ru.vldf.sportsportal.mapper.sectional.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.booking.OrderEntity;
import ru.vldf.sportsportal.domain.sectional.booking.ReservationEntity;
import ru.vldf.sportsportal.dto.payment.PaymentRequestDTO;
import ru.vldf.sportsportal.dto.sectional.booking.OrderDTO;
import ru.vldf.sportsportal.dto.sectional.booking.links.OrderLinkDTO;
import ru.vldf.sportsportal.dto.sectional.booking.shortcut.OrderShortDTO;
import ru.vldf.sportsportal.mapper.general.AbstractOverallVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.booking.OrderURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Mapper(uses = {UserMapper.class, ReservationMapper.class, OrderURLMapper.class, JavaTimeMapper.class})
public abstract class OrderMapper extends AbstractOverallVersionedMapper<OrderEntity, OrderDTO, OrderShortDTO, OrderLinkDTO> {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "customer", ignore = true),
            @Mapping(target = "reservations", ignore = true),
            @Mapping(target = "isOwned", ignore = true),
            @Mapping(target = "isFreed", ignore = true)
    })
    public abstract OrderEntity toEntity(OrderDTO dto);


    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    public abstract OrderLinkDTO toLinkDTO(OrderEntity entity);

    @Mappings({
            @Mapping(target = "sum", ignore = true)
    })
    public abstract OrderEntity toLinkEntity(OrderLinkDTO dto);


    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    public abstract OrderShortDTO toShortDTO(OrderEntity entity);


    @Mappings({
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "email", source = "customer.email")
    })
    public abstract PaymentRequestDTO toPayment(OrderEntity entity);


    public OrderDTO toDTO(OrderEntity entity, URI paymentLink) {
        OrderDTO dto = toDTO(entity);
        dto.setPaymentLink(!entity.getIsOwned() ? paymentLink : null);
        return dto;
    }


    @Override
    public OrderEntity merge(OrderEntity acceptor, OrderEntity donor) throws OptimisticLockException {
        super.merge(acceptor, donor);

        acceptor.setSum(donor.getSum());
        acceptor.setIsPaid(donor.getIsPaid());
        acceptor.setDatetime(donor.getDatetime());
        acceptor.setExpiration(donor.getExpiration());
        acceptor.setCustomer(donor.getCustomer());

        Collection<ReservationEntity> reservations = acceptor.getReservations();
        if (!reservations.isEmpty()) {
            reservations.clear();
        }
        for (ReservationEntity reservation : donor.getReservations()) {
            reservation.setOrder(acceptor);
            reservations.add(reservation);
        }

        return acceptor;
    }
}
