package ru.vldf.sportsportal.integration.robokassa.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.vldf.sportsportal.dto.validation.annotations.Email;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentParams {

    @NotBlank
    @JsonProperty("MerchantLogin")
    private String merchantLogin;

    @Digits(integer = 6, fraction = 2)
    @JsonProperty("OutSum")
    private BigDecimal outSum;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("InvDesc")
    private String invDesc;

    @NotBlank
    @JsonProperty("SignatureValue")
    private String signatureValue;

    @Min(0)
    @JsonProperty("InvId")
    private Integer invId;

    @Email
    @JsonProperty("Email")
    private String email;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("ExpirationDate")
    private Date expirationDate;
}
