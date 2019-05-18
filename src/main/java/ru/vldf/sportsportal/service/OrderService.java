package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.domain.sectional.booking.OrderEntity;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.payment.PaymentCheckDTO;
import ru.vldf.sportsportal.dto.sectional.booking.OrderDTO;
import ru.vldf.sportsportal.dto.sectional.booking.shortcut.OrderShortDTO;
import ru.vldf.sportsportal.integration.payment.RobokassaSecurityException;
import ru.vldf.sportsportal.integration.payment.RobokassaService;
import ru.vldf.sportsportal.mapper.sectional.booking.OrderMapper;
import ru.vldf.sportsportal.repository.booking.OrderRepository;
import ru.vldf.sportsportal.service.general.AbstractSecurityService;
import ru.vldf.sportsportal.service.general.CRUDService;
import ru.vldf.sportsportal.service.general.throwable.ForbiddenAccessException;
import ru.vldf.sportsportal.service.general.throwable.ResourceCannotUpdateException;
import ru.vldf.sportsportal.service.general.throwable.ResourceNotFoundException;
import ru.vldf.sportsportal.service.general.throwable.UnauthorizedAccessException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

/**
 * @author Namednev Artem
 */
@Service
public class OrderService extends AbstractSecurityService implements CRUDService<OrderEntity, OrderDTO> {

    private final RobokassaService robokassaService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    @Autowired
    public OrderService(RobokassaService robokassaService, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.robokassaService = robokassaService;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }


    @PostConstruct
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void clearUnpaid() {
        orderRepository.deleteAll(orderRepository.findAllByIsPaidIsFalse());
    }


    /**
     * Returns requested page with current user's orders.
     *
     * @param dividerDTO the page divider.
     * @return requested page with orders.
     * @throws UnauthorizedAccessException if authorization is missing.
     */
    @Transactional(readOnly = true)
    public PageDTO<OrderShortDTO> getList(PageDividerDTO dividerDTO) throws UnauthorizedAccessException {
        return PageDTO.from(orderRepository.findAllByCustomer(getCurrentUserEntity(), new PageDivider(dividerDTO).getPageRequest()).map(orderMapper::toShortDTO));
    }

    /**
     * Returns requested order by order identifier.
     *
     * @param id the order identifier.
     * @return requested order data.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to get this order.
     * @throws ResourceNotFoundException   if order not found.
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public OrderDTO get(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if ((!currentUserIsAdmin()) && (!isCurrentUser(orderEntity.getCustomer()))) {
                throw new ForbiddenAccessException(msg("sportsportal.booking.Order.forbidden.message"));
            } else {
                return orderMapper.toDTO(orderEntity, robokassaService.computeLink(orderMapper.toPayment(orderEntity)));
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.booking.Order.notExistById.message", id), e);
        }
    }

    /**
     * Create new order and returns its identifier.
     *
     * @param orderDTO the new order data.
     * @return created new order identifier.
     */
    @Transactional
    public Integer create(OrderDTO orderDTO) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "create"));
    }

    /**
     * Update order data.
     *
     * @param id       the order identifier.
     * @param orderDTO new order data.
     */
    @Transactional
    public void update(Integer id, OrderDTO orderDTO) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "update"));
    }

    /**
     * Order payment checkout.
     *
     * @param check the payment check.
     * @return payment success code.
     * @throws ForbiddenAccessException      if security check failed.
     * @throws ResourceNotFoundException     if order not found.
     * @throws ResourceCannotUpdateException if order cannot be paid.
     */
    @Transactional(
            rollbackFor = {ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class, RobokassaSecurityException.class}
    )
    public String pay(PaymentCheckDTO check) throws ForbiddenAccessException, ResourceNotFoundException, ResourceCannotUpdateException {
        Integer id = 0; // invalid id
        try {
            id = robokassaService.payment(check);
            OrderEntity orderEntity = orderRepository.getOne(id);
            if (orderEntity.getIsPaid()) {
                throw new ResourceCannotUpdateException(msg("sportsportal.booking.Order.alreadyPaid.message", id));
            } else {
                orderEntity.setIsPaid(true);
                orderEntity.setExpiration(null);
                orderRepository.save(orderEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.booking.Order.notExistById.message", id), e);
        } catch (RobokassaSecurityException e) {
            throw new ForbiddenAccessException(msg("sportsportal.booking.Order.cannotPaid.message", e.getInvId()), e);
        }
        return "OK" + id;
    }

    /**
     * Delete order.
     *
     * @param id the order identifier.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this order.
     * @throws ResourceNotFoundException   if order not found.
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if ((!currentUserIsAdmin()) && (!isCurrentUser(orderEntity.getCustomer()))) {
                throw new ForbiddenAccessException(msg("sportsportal.booking.Order.forbidden.message"));
            } else {
                orderRepository.delete(orderEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.booking.Order.notExistById.message", id), e);
        }
    }
}
