package ru.vldf.sportsportal.integration.robokassa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vldf.sportsportal.integration.robokassa.model.Payment;
import ru.vldf.sportsportal.integration.robokassa.model.PaymentParams;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class RobokassaService {

    private static final TypeReference mapTypeReference = new TypeReference<Map<String, Object>>() {
    };

    private static final String BASE = "https://auth.robokassa.ru/Merchant/Index.aspx";
    private static final String TEST_KEY = "IsTest";
    private static final Integer TEST_VALUE = 1;

    private final boolean testing;
    private final String password;
    private final String login;

    private final ObjectMapper mapper;
    private final URI url;


    @Autowired
    public RobokassaService(
            @Value("${robokassa.testing:false}") boolean testing,
            @Value("${robokassa.password.one}") String password,
            @Value("${robokassa.login}") String login
    ) throws URISyntaxException {
        this.mapper = new ObjectMapper();
        this.url = new URI(BASE);
        this.testing = testing;
        this.password = password;
        this.login = login;
    }


    private static String computeSign(Integer id, BigDecimal sum, String login, String password) {
        Assert.notNull(id, "id must be present");
        Assert.notNull(sum, "sum must be present");
        Assert.isTrue(StringUtils.hasText(login), "login must not be blank");
        Assert.isTrue(StringUtils.hasText(password), "password must not be blank");
        return Sha512DigestUtils.shaHex(login.trim() + ":" + sum.toString() + ":" + id.toString() + ":" + password.trim());
    }


    public URI computeLink(Payment payment) {
        return computeLink(generateParams(payment));
    }


    private PaymentParams generateParams(Payment payment) {
        Integer id = payment.getId();
        BigDecimal sum = payment.getSum();
        String desc = payment.getDescription();

        PaymentParams params = new PaymentParams();

        // main
        params.setInvId(id);
        params.setOutSum(sum);
        params.setInvDesc(desc);
        params.setMerchantLogin(login);
        params.setSignatureValue(computeSign(id, sum));

        // additional
        params.setEmail(payment.getEmail());
        params.setExpirationDate(payment.getExpiration());

        return params;
    }

    private String computeSign(Integer id, BigDecimal sum) {
        return computeSign(id, sum, login, password);
    }

    private URI computeLink(@Validated PaymentParams params) {
        Map<String, Object> variables = mapper.convertValue(params, mapTypeReference);
        if (testing) variables.put(TEST_KEY, TEST_VALUE);
        return UriComponentsBuilder.fromUri(url).build(variables);
    }
}
