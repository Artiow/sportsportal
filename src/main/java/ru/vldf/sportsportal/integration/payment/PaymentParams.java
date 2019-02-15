package ru.vldf.sportsportal.integration.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Namednev Artem
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
class PaymentParams {

    @JsonProperty("MerchantLogin")
    private String merchantLogin;

    @JsonProperty("OutSum")
    private BigDecimal outSum;

    @JsonProperty("InvDesc")
    private String invDesc;

    @JsonProperty("SignatureValue")
    private String signatureValue;

    @JsonProperty("InvId")
    private Integer invId;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("ExpirationDate")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date expirationDate;
}
