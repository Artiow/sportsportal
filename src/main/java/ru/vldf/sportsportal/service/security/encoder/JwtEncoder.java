package ru.vldf.sportsportal.service.security.encoder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.util.RandomCharsSequenceGenerator;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.Map;

@Service
public class JwtEncoder implements Encoder {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private ExpiringClockProvider clock;

    @Value("${jwt.issuer}")
    private String issuer;
    private String sign;

    @Autowired
    public void setClock(ExpiringClockProvider clock) {
        this.clock = clock;
    }


    /**
     * Secret key Base64 generate and encoding.
     */
    @PostConstruct
    private void init() {
        sign = TextCodec.BASE64.encode(RandomCharsSequenceGenerator.generate(16));
    }


    /**
     * Generate JWT.
     *
     * @param payload {@link Map} token payload
     * @param type    {@link ExpirationType} token expiration type
     * @return {@link String} encoded json
     */
    public String generate(final Map<String, Object> payload, final ExpirationType type) throws JwtException {
        final Pair<Date, Date> date = clock
                .gen(type);

        final Claims claims = Jwts
                .claims(payload)
                .setIssuer(issuer)
                .setIssuedAt(date.getFirst())
                .setExpiration(date.getSecond());

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, sign)
                .compact();
    }

    @Override
    public String getAccessToken(final Map<String, Object> payload) throws JwtException {
        return generate(payload, ExpirationType.ACCESS);
    }

    @Override
    public String getRefreshToken(final Map<String, Object> payload) throws JwtException {
        return generate(payload, ExpirationType.REFRESH);
    }

    @Override
    public Map<String, Object> verify(final String token) throws JwtException {
        return Jwts
                .parser()
                .requireIssuer(issuer)
                .setSigningKey(sign)
                .parseClaimsJws(token)
                .getBody();
    }
}
