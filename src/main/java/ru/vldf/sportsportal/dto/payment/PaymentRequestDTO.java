package ru.vldf.sportsportal.dto.payment;

import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.validation.annotations.Email;
import ru.vldf.sportsportal.dto.validation.annotations.Future;
import ru.vldf.sportsportal.dto.validation.annotations.NullOrNotBlank;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PaymentRequestDTO {

    @Min(0)
    @NotNull
    private Integer id;

    @Min(0)
    @NotNull
    @Digits(integer = 6, fraction = 2)
    private BigDecimal sum;

    @Size(max = 100)
    @NullOrNotBlank
    private String description;


    @Email
    private String email;

    @Future
    private Date expiration;
}
