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
import ru.vldf.sportsportal.util.CharSequenceGenerator;

import java.util.Date;
import java.util.Map;

/**
 * @author Namednev Artem
 */
@Service
public class JwtEncoder implements Encoder {

    private final static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private final ExpiringClockProvider clock;

    private final String issuer;
    private final String sign;

    @Autowired
    public JwtEncoder(
            @Value("${jwt.issuer}") String issuer,
            ExpiringClockProvider clock
    ) {
        this.sign = TextCodec.BASE64.encode(CharSequenceGenerator.generate(16));
        this.issuer = issuer;
        this.clock = clock;
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
