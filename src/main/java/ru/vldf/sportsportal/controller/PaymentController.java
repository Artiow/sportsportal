package ru.vldf.sportsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.vldf.sportsportal.dto.payment.PaymentCheckDTO;
import ru.vldf.sportsportal.integration.payment.RobokassaService;

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
    public PaymentController(RobokassaService robokassaService) {
        this.robokassaService = robokassaService;
    }


    @GetMapping("/anonymous")
    @ApiOperation("анонимный платеж")
    public ResponseEntity<String> payment(
            @RequestParam @NotNull @Min(0) @Digits(integer = 6, fraction = 2) BigDecimal sum
    ) {
        return ResponseEntity.ok(robokassaService.computeLink(sum).toString());
    }

    @PostMapping("/result")
    @ApiOperation("результат платежа")
    public ResponseEntity<String> result(
            @RequestParam(value = "InvId") Integer id,
            @RequestParam(value = "OutSum") BigDecimal sum,
            @RequestParam(value = "Fee") BigDecimal fee,
            @RequestParam(value = "Email") String email,
            @RequestParam(value = "SignatureValue") String sign
    ) {
        PaymentCheckDTO paymentCheckDTO = new PaymentCheckDTO();
        paymentCheckDTO.setInvId(id);
        paymentCheckDTO.setOutSum(sum);
        paymentCheckDTO.setFee(fee);
        paymentCheckDTO.setEmail(email);
        paymentCheckDTO.setSignatureValue(sign);
        // todo: payment!
        return ResponseEntity.ok("OK" + id);
    }
}
