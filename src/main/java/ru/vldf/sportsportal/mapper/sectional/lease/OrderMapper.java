package ru.vldf.sportsportal.mapper.sectional.lease;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.domain.sectional.lease.ReservationEntity;
import ru.vldf.sportsportal.dto.payment.PaymentRequestDTO;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.dto.sectional.lease.links.OrderLinkDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.OrderShortDTO;
import ru.vldf.sportsportal.mapper.generic.AbstractVersionedMapper;
import ru.vldf.sportsportal.mapper.manual.JavaTimeMapper;
import ru.vldf.sportsportal.mapper.manual.url.lease.OrderURLMapper;
import ru.vldf.sportsportal.mapper.sectional.common.UserMapper;

import javax.persistence.OptimisticLockException;
import java.net.URI;
import java.util.Collection;

/**
 * @author Namednev Artem
 */
@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, ReservationMapper.class, OrderURLMapper.class, JavaTimeMapper.class}
)
public abstract class OrderMapper extends AbstractVersionedMapper<OrderEntity, OrderDTO> {

    @Mappings({
            @Mapping(target = "description", ignore = true),
            @Mapping(target = "email", source = "customer.email")
    })
    public abstract PaymentRequestDTO toPayment(OrderEntity entity);

    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    public abstract OrderShortDTO toShortDTO(OrderEntity entity);

    @Mappings({
            @Mapping(target = "orderURL", source = "id", qualifiedByName = {"toOrderURL", "fromId"}),
            @Mapping(target = "customerURL", source = "customer", qualifiedByName = {"toUserURL", "fromEntity"})
    })
    public abstract OrderLinkDTO toLinkDTO(OrderEntity entity);

    @Mapping(target = "sum", ignore = true)
    public abstract OrderEntity toEntity(OrderLinkDTO dto);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "customer", ignore = true),
            @Mapping(target = "reservations", ignore = true),
            @Mapping(target = "isOwnerOccupied", ignore = true)
    })
    public abstract OrderEntity toEntity(OrderDTO dto);


    public OrderDTO toDTO(OrderEntity entity, URI paymentLink) {
        OrderDTO dto = toDTO(entity);
        dto.setPaymentLink(!entity.getIsOwnerOccupied() ? paymentLink : null);
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
