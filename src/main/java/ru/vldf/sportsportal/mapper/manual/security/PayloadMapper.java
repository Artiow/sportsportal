package ru.vldf.sportsportal.mapper.manual.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.service.security.model.Payload;

import java.util.Map;

/**
 * @author Namednev Artem
 */
@Component
public class PayloadMapper {

    private final static ObjectMapper MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final static TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
    };


    public Map<String, Object> toMap(Payload payload) {
        return MAPPER.convertValue(payload, MAP_TYPE_REFERENCE);
    }

    public Payload toPayload(Map<String, Object> map) {
        return MAPPER.convertValue(map, Payload.class);
    }
}
