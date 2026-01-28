package io.securepath.authsphere.authserver.service;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JWTToken {

    private final JwtEncoder jwtEncoder;

    public JWTToken(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(String username, String userId, String environment, String userIp) {
        Instant now = Instant.now();
        long expiry = 3600L; // 1 hour

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("authsphere")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(username)
                .claim("userId", userId)
                .claim("environment", environment)
                .claim("userIp", userIp)
                .claim("roles", List.of("ADMIN"))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
