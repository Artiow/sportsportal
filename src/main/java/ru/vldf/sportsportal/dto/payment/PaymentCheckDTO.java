package ru.vldf.sportsportal.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ru.vldf.sportsportal.dto.validation.annotations.Email;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
public class PaymentCheckDTO {

    @NotNull
    @Min(0)
    @Digits(integer = 6, fraction = 2)
    @JsonProperty("OutSum")
    private BigDecimal outSum;

    @Min(0)
    @JsonProperty("InvId")
    private Integer invId;

    @Email
    @JsonProperty("Email")
    private String email;

    @NotNull
    @Min(0)
    @Digits(integer = 6, fraction = 2)
    @JsonProperty("Fee")
    private BigDecimal fee;

    @NotBlank
    @JsonProperty("SignatureValue")
    private String signatureValue;
}
