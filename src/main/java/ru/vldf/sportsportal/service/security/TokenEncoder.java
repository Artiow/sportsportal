package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.util.RandomCharsSequenceGenerator;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenEncoder implements ExpiringClock {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private TimeUnit accessTokenUnit;
    private Long accessTokenLifetime;
    private TimeUnit refreshTokenUnit;
    private Long refreshTokenLifetime;

    private String tokenType;
    private String issuer;
    private String sign;


    /**
     * Getting used token type.
     *
     * @return tokenType
     */
    public String getTokenType() {
        return tokenType;
    }

    @Value("${jwt.token-type}")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Value("${jwt.issuer}")
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Value("${jwt.access.lifetime.amount}")
    public void setAccessTokenLifetime(Long accessTokenLifetime) {
        this.accessTokenLifetime = accessTokenLifetime;
    }

    @Value("${jwt.access.lifetime.unit}")
    public void setAccessTokenUnit(TimeUnit accessTokenUnit) {
        this.accessTokenUnit = accessTokenUnit;
    }

    @Value("${jwt.refresh.lifetime.amount}")
    public void setRefreshTokenLifetime(Long refreshTokenLifetime) {
        this.refreshTokenLifetime = refreshTokenLifetime;
    }

    @Value("${jwt.refresh.lifetime.unit}")
    public void setRefreshTokenUnit(TimeUnit refreshTokenUnit) {
        this.refreshTokenUnit = refreshTokenUnit;
    }


    /**
     * Secret key Base64 generate and encoding.
     * Tokens lifetimes calculating.
     */
    @PostConstruct
    private void init() {
        accessTokenLifetime = accessTokenUnit.toMillis(accessTokenLifetime);
        refreshTokenLifetime = refreshTokenUnit.toMillis(refreshTokenLifetime);
        sign = TextCodec.BASE64.encode(RandomCharsSequenceGenerator.generate(16));
    }


    /**
     * Generate access JWT.
     *
     * @param payload {@link Map} token payload
     * @return {@link String} encoded json
     */
    public String getAccessToken(final Map<String, Object> payload) throws JwtException {
        return generate(payload, ExpirationType.ACCESS);
    }

    /**
     * Generate refresh JWT.
     *
     * @param payload {@link Map} token payload
     * @return {@link String} encoded json
     */
    public String getRefreshToken(final Map<String, Object> payload) throws JwtException {
        return generate(payload, ExpirationType.REFRESH);
    }

    /**
     * Generate JWT.
     *
     * @param payload {@link Map} token payload
     * @param type    {@link ExpirationType} token expiration type
     * @return {@link String} encoded json
     */
    public String generate(final Map<String, Object> payload, final ExpirationType type) throws JwtException {
        final Date now
                = now();

        final Claims claims = Jwts
                .claims(payload)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(exp(now, type));

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, sign)
                .compact();
    }

    /**
     * Returns payload of parsed JWT.
     *
     * @param token {@link String} JWT
     * @return map of token payload
     */
    public Map<String, Object> verify(final String token) throws JwtException {
        return Jwts
                .parser()
                .requireIssuer(issuer)
                .setSigningKey(sign)
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Date now() {
        return Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Date exp(Date now, ExpirationType type) {
        Long lifetime;
        switch (type) {
            case ACCESS:
                lifetime = accessTokenLifetime;
                break;
            case REFRESH:
                lifetime = refreshTokenLifetime;
                break;
            default:
                lifetime = 0L;
        }
        return new Date(now.getTime() + lifetime);
    }
}
