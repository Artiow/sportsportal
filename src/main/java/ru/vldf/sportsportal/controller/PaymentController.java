package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vldf.sportsportal.integration.robokassa.RobokassaService;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Namednev Artem
 */
@Validated
@RestController
@Api(tags = {"Payment"})
@RequestMapping("${api.path.payment}")
public class PaymentController {

    private final RobokassaService robokassaService;

    @Autowired
    public PaymentController(
            RobokassaService robokassaService
    ) {
        this.robokassaService = robokassaService;
    }

    @GetMapping("/anonymous")
    @ApiOperation("анонимный платеж")
    public ResponseEntity<String> payment(
            @RequestParam @NotNull @Min(0) @Digits(integer = 6, fraction = 2) BigDecimal sum
    ) {
        return ResponseEntity.ok(robokassaService.computeLink(sum).toString());
    }
}
