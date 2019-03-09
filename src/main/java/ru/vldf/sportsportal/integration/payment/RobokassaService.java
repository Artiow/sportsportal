package ru.vldf.sportsportal.integration.payment;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vldf.sportsportal.dto.payment.PaymentCheckDTO;
import ru.vldf.sportsportal.dto.payment.PaymentRequestDTO;
import ru.vldf.sportsportal.service.generic.AbstractMessageService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Namednev Artem
 */
@Slf4j
@Service
public class RobokassaService extends AbstractMessageService {

    private static final TypeReference MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
    };

    private static final Integer ANONYMOUS_PAYMENT_ID = 0;
    private static final String ANONYMOUS_PAYMENT_DESC = "sportsportal.robokassa.anonymous.description";
    private static final String IDENTIFIED_PAYMENT_DESC = "sportsportal.robokassa.identified.description";

    private static final String GENERATE_EX_MSG = "sportsportal.payment.generate.message";
    private static final String SECURITY_EX_MSG = "sportsportal.payment.security.message";

    private static final String BASE = "https://auth.robokassa.ru/Merchant/Index.aspx";
    private static final String TEST_KEY = "IsTest";
    private static final Integer TEST_VALUE = 1;

    private final Validator validator;

    private final boolean testing;
    private final String password;
    private final String login;

    private final ObjectMapper mapper;
    private final URI url;


    @Autowired
    public RobokassaService(
            Validator validator,
            @Value("${robokassa.testing:false}") boolean testing,
            @Value("${robokassa.password.one}") String password,
            @Value("${robokassa.login}") String login
    ) throws URISyntaxException {
        this.validator = validator;
        this.mapper = new ObjectMapper();
        this.url = new URI(BASE);
        this.testing = testing;
        this.password = password;
        this.login = login;
    }


    private static String computeSign(Integer id, BigDecimal sum, String login, String password) {
        Assert.notNull(id, "id must be present");
        Assert.notNull(sum, "sum must be present");
        Assert.isTrue(!(id < 0), "id must not be less than 0");
        Assert.isTrue(!(sum.compareTo(BigDecimal.ZERO) < 0), "sum must not be less than 0");
        Assert.isTrue(StringUtils.hasText(login), "login must not be blank");
        Assert.isTrue(StringUtils.hasText(password), "password must not be blank");
        return Sha512DigestUtils.shaHex(login.trim() + ":" + sum.toString() + ":" + id.toString() + ":" + password.trim());
    }


    public URI computeLink(BigDecimal sum) {
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setSum(sum);
        request.setId(ANONYMOUS_PAYMENT_ID);
        request.setDescription(msg(ANONYMOUS_PAYMENT_DESC));
        return computeLink(request);
    }

    public URI computeLink(PaymentRequestDTO request) {
        validate(request);

        Integer id = request.getId();
        BigDecimal sum = request.getSum();
        String desc = Optional.ofNullable(request.getDescription()).orElse(description(sum));

        PaymentParams params = new PaymentParams();

        // required
        params.setInvId(id);
        params.setOutSum(sum);
        params.setInvDesc(desc);
        params.setMerchantLogin(login);
        params.setSignatureValue(computeSign(id, sum));

        // additional
        params.setEmail(request.getEmail());
        params.setExpirationDate(request.getExpiration());

        return computeLink(params);
    }

    public Integer payment(PaymentCheckDTO check) {
        validate(check);

        Integer id = check.getInvId();

        if (!Objects.equals(
                check.getSignatureValue(),
                computeSign(id, check.getOutSum())
        )) {
            throw new RobokassaSecurityException(msg(SECURITY_EX_MSG, id), id);
        }

        return id;
    }


    private <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!CollectionUtils.isEmpty(violations)) {
            StringBuilder builder = new StringBuilder();
            Iterator<ConstraintViolation<T>> i = violations.iterator();
            while (i.hasNext()) {
                builder.append(i.next().getMessage());
                if (i.hasNext()) builder.append(", ");
            }
            throw new RobokassaGenerateException(msg(GENERATE_EX_MSG, obj.getClass().getName(), builder.toString()));
        }
    }

    private String description(BigDecimal sum) {
        return msg(IDENTIFIED_PAYMENT_DESC, sum.toString());
    }

    private String computeSign(Integer id, BigDecimal sum) {
        return computeSign(id, sum, login, password);
    }


    private URI computeLink(PaymentParams params) {
        Map<String, Object> variables = mapper.convertValue(params, MAP_TYPE_REFERENCE);
        if (testing) variables.put(TEST_KEY, TEST_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(url);
        variables.forEach(builder::queryParam);
        return builder.build().toUri();
    }
}
