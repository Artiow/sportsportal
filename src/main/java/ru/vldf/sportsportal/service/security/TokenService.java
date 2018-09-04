package ru.vldf.sportsportal.service.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class TokenService implements Clock {

    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

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


    /**
     * Secret key Base64 generate and encoding.
     */
    @PostConstruct
    private void init() {
        int len = 16; // sign length
        StringBuilder signBuilder = new StringBuilder(len);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            signBuilder.append(chars.charAt(random.nextInt(chars.length())));
        }
        sign = TextCodec.BASE64.encode(signBuilder.toString());
    }


    /**
     * Returns JWT.
     *
     * @param attributes - map of token data
     * @return encoded json
     */
    public String generate(final Map<String, Object> attributes) throws JwtException {
        final Claims claims = Jwts
                .claims(attributes)
                .setIssuer(issuer)
                .setIssuedAt(now());

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
}
