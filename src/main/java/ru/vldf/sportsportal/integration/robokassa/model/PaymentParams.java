package ru.vldf.sportsportal.integration.robokassa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class PaymentParams {

    @NotBlank
    @JsonProperty("MerchantLogin")
    private String merchantLogin;

    @NotBlank
    @JsonProperty("OutSum")
    private String outSum;

    @NotBlank
    @Size(max = 100)
    @JsonProperty("InvDesc")
    private String invDesc;

    @NotBlank
    @JsonProperty("SignatureValue")
    private String signatureValue;
}
