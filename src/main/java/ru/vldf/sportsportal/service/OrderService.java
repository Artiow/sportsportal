package ru.vldf.sportsportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vldf.sportsportal.config.messages.MessageContainer;
import ru.vldf.sportsportal.domain.sectional.lease.OrderEntity;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.mapper.sectional.lease.OrderMapper;
import ru.vldf.sportsportal.repository.common.RoleRepository;
import ru.vldf.sportsportal.repository.common.UserRepository;
import ru.vldf.sportsportal.repository.lease.OrderRepository;
import ru.vldf.sportsportal.service.generic.*;

import javax.persistence.EntityNotFoundException;

@Service
public class OrderService extends AbstractSecurityService implements AbstractCRUDService<OrderEntity, OrderDTO> {

    @Value("${code.role.admin}")
    private String adminRoleCode;

    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    @Autowired
    public OrderService(MessageContainer messages, UserRepository userRepository, RoleRepository roleRepository) {
        super(messages, userRepository, roleRepository);
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }


    /**
     * Returns requested order.
     *
     * @param id {@link Integer} order identifier
     * @return {@link OrderDTO} order
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to get this order
     * @throws ResourceNotFoundException   if order not found
     */
    @Transactional(
            readOnly = true,
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public OrderDTO get(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isCurrentUser(orderEntity.getCustomer()))) {
                throw new ForbiddenAccessException(msg("sportsportal.lease.Order.forbidden.message"));
            } else {
                return orderMapper.toDTO(orderEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Order.notExistById.message", id), e);
        }
    }

    /**
     * Create new order and returns its identifier.
     *
     * @param orderDTO {@link OrderDTO} order data
     * @return new order {@link Integer} identifier
     */
    @Transactional
    public Integer create(OrderDTO orderDTO) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "create"));
    }

    /**
     * Update order data.
     *
     * @param id       {@link Integer} order identifier
     * @param orderDTO {@link OrderDTO} new order data
     */
    @Transactional
    public void update(Integer id, OrderDTO orderDTO) {
        throw new UnsupportedOperationException(msg("sportsportal.handle.UnsupportedOperationException.message", "update"));
    }

    /**
     * Order payment.
     *
     * @param id {@link Integer} order identifier
     * @throws UnauthorizedAccessException   if authorization is missing
     * @throws ForbiddenAccessException      if user don't have permission to pay this order
     * @throws ResourceNotFoundException     if order not found
     * @throws ResourceCannotUpdateException if order cannot be paid
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class, ResourceCannotUpdateException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    // todo: remove suppress warning
    @SuppressWarnings("unused")
    public void payFor(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException, ResourceCannotUpdateException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isCurrentUser(orderEntity.getCustomer()))) {
                throw new ForbiddenAccessException(msg("sportsportal.lease.Order.forbidden.message"));
            } else if (orderEntity.getPaid()) {
                throw new ResourceCannotUpdateException(msg("sportsportal.lease.Order.alreadyPaid.message", id));
            } else {
                // todo: payment here!
                // orderEntity.setPaid(true);
                // orderEntity.setExpiration(null);
                orderRepository.save(orderEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Order.notExistById.message", id), e);
        }
    }

    /**
     * Delete order.
     *
     * @param id {@link Integer} order identifier
     * @throws UnauthorizedAccessException if authorization is missing
     * @throws ForbiddenAccessException    if user don't have permission to delete this order
     * @throws ResourceNotFoundException   if order not found
     */
    @Transactional(
            rollbackFor = {UnauthorizedAccessException.class, ForbiddenAccessException.class, ResourceNotFoundException.class},
            noRollbackFor = {EntityNotFoundException.class}
    )
    public void delete(Integer id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        try {
            OrderEntity orderEntity = orderRepository.getOne(id);
            if (!currentUserHasRoleByCode(adminRoleCode) && (!isCurrentUser(orderEntity.getCustomer()))) {
                throw new ForbiddenAccessException(msg("sportsportal.lease.Order.forbidden.message"));
            } else {
                orderRepository.delete(orderEntity);
            }
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(msg("sportsportal.lease.Order.notExistById.message", id), e);
        }
    }
}
