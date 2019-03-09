package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.pagination.PageDTO;
import ru.vldf.sportsportal.dto.pagination.filters.generic.PageDividerDTO;
import ru.vldf.sportsportal.dto.sectional.lease.OrderDTO;
import ru.vldf.sportsportal.dto.sectional.lease.shortcut.OrderShortDTO;
import ru.vldf.sportsportal.service.OrderService;
import ru.vldf.sportsportal.service.generic.ForbiddenAccessException;
import ru.vldf.sportsportal.service.generic.ResourceNotFoundException;
import ru.vldf.sportsportal.service.generic.UnauthorizedAccessException;

/**
 * @author Namednev Artem
 */
@RestController
@Api(tags = {"Order"})
@RequestMapping("${api.path.lease.order}")
public class OrderController {

    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    /**
     * Returns requested page with orders for current user.
     *
     * @param pageSize the page size.
     * @param pageNum  the page number.
     * @return page with list of orders.
     * @throws UnauthorizedAccessException if authorization is missing.
     */
    @GetMapping("/list")
    @ApiOperation("получить список своих заказов")
    public PageDTO<OrderShortDTO> getList(
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNum
    ) throws UnauthorizedAccessException {
        PageDividerDTO pageDividerDTO = new PageDividerDTO();
        pageDividerDTO.setPageSize(pageSize);
        pageDividerDTO.setPageNum(pageNum);
        return orderService.getList(pageDividerDTO);
    }

    /**
     * Returns order by identifier with full information.
     *
     * @param id the order identifier.
     * @return requested order.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to get this order.
     * @throws ResourceNotFoundException   if order not found.
     */
    @GetMapping("/{id}")
    @ApiOperation("получить информацию о заказе")
    public OrderDTO get(@PathVariable int id) throws UnauthorizedAccessException, ForbiddenAccessException, ResourceNotFoundException {
        return orderService.get(id);
    }

    /**
     * Delete order by id.
     *
     * @param id the order identifier.
     * @return no content.
     * @throws ResourceNotFoundException   if order not found.
     * @throws UnauthorizedAccessException if authorization is missing.
     * @throws ForbiddenAccessException    if user don't have permission to delete this order.
     */
    @DeleteMapping("/{id}")
    @ApiOperation("удалить заказ")
    public ResponseEntity<Void> delete(@PathVariable int id) throws ResourceNotFoundException, UnauthorizedAccessException, ForbiddenAccessException {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
