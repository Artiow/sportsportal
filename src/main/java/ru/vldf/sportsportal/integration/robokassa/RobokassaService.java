package ru.vldf.sportsportal.integration.robokassa;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import ru.vldf.sportsportal.integration.robokassa.model.PaymentParams;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class RobokassaService {

    private static final TypeReference mapTypeReference = new TypeReference<Map<String, Object>>() {
    };

    private static final String base = "https://auth.robokassa.ru/Merchant/Index.aspx";

    private final String url;
    private final RestOperations rest;
    private final ObjectMapper mapper;


    @Autowired
    public RobokassaService(RestOperations rest) throws URISyntaxException {
        this.url = new URI(base).toString();
        this.mapper = new ObjectMapper();
        this.rest = rest;
    }


    private void pay(PaymentParams params) {
        // noinspection ConstantConditions, unchecked
        rest.postForLocation(url, null, (Map<String, Object>) mapper.convertValue(params, mapTypeReference));
    }
}
