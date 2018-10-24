package ru.vldf.sportsportal.mapper.manual.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import ru.vldf.sportsportal.dto.security.JwtPayload;

import java.util.Map;

@Component
public class JwtPayloadMapper {

    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private static final TypeReference<Map<String, Object>> mapTypeReference = new TypeReference<Map<String, Object>>() {

    };


    public Map<String, Object> toMap(JwtPayload payload) {
        return mapper.convertValue(payload, mapTypeReference);
    }

    public JwtPayload toJwtPayload(Map<String, Object> map) {
        return mapper.convertValue(map, JwtPayload.class);
    }
}
