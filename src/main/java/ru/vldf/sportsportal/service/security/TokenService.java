package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vldf.sportsportal.util.RandomCharsSequenceGenerator;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService implements Clock {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    private String accessTokenUnit;
    private Long accessTokenLifetime;
    private String refreshTokenUnit;
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
    public void setAccessTokenUnit(String accessTokenUnit) {
        this.accessTokenUnit = accessTokenUnit;
    }

    @Value("${jwt.refresh.lifetime.amount}")
    public void setRefreshTokenLifetime(Long refreshTokenLifetime) {
        this.refreshTokenLifetime = refreshTokenLifetime;
    }

    @Value("${jwt.refresh.lifetime.unit}")
    public void setRefreshTokenUnit(String refreshTokenUnit) {
        this.refreshTokenUnit = refreshTokenUnit;
    }


    /**
     * Secret key Base64 generate and encoding.
     * Tokens lifetimes calculating.
     */
    @PostConstruct
    private void init() {
        sign = TextCodec.BASE64.encode(RandomCharsSequenceGenerator.generate(16));
        accessTokenLifetime = TimeUnit.valueOf(accessTokenUnit).toMillis(accessTokenLifetime);
        refreshTokenLifetime = TimeUnit.valueOf(refreshTokenUnit).toMillis(refreshTokenLifetime);
    }


    /**
     * Generate access JWT.
     *
     * @param attributes {@link Map} token data
     * @return {@link String} encoded json
     */
    public String genAccessToken(final Map<String, Object> attributes) throws JwtException {
        final Date now
                = now();

        final Claims claims = Jwts
                .claims(attributes)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(exp(now, refreshTokenLifetime));

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, sign)
                .compact();
    }

    /**
     * Generate refresh JWT.
     *
     * @param id {@link Integer} token data
     * @return {@link String} encoded json
     */
    public String genRefreshToken(final Integer id) throws JwtException {
        final Date now
                = now();

        final Claims claims = Jwts
                .claims(Collections.singletonMap("id", id))
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(exp(now, refreshTokenLifetime));

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, sign)
                .compact();
    }

    /**
     * Returns encoded and parsed JWT.
     *
     * @param token - encoded json
     * @return map of token data
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

    public Date exp(Date now, long exp) {
        return new Date(now.getTime() + exp);
    }
}
