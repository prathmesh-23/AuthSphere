package io.securepath.authsphere.IOJwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.securepath.authsphere.models.Users;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JwtUtility {

    private static final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(JWTConstant.SECRET));

    // Create token from claims
    public static String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate token and return claims
    public static String validateToken(String token) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // parses + validates signature + expiration

        } catch (ExpiredJwtException e) {
            System.err.println("❌ Token expired: " + e.getMessage());
            return "Token expired";
        } catch (UnsupportedJwtException e) {
            System.err.println("❌ Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.err.println("❌ Malformed JWT: " + e.getMessage());
        } catch (SignatureException e) {
            System.err.println("❌ Invalid signature: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("❌ Empty or null token: " + e.getMessage());
            return "INVALID SESSION";
        }
        return "SUCCESS";
    }

    public static String getToken(String pAuth) {
        return pAuth.substring(7);
    }

    public static Claims getClaimsFromToken(String pToken) {
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(pToken);

        return jws.getBody();
    }

    public static String setClaims(Map<String, Object> pClaims, Users pUser) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JWTClaim.USERID, pUser.getUserid());
        claims.put(JWTClaim.SUBJECT, pUser.getUserName());
        claims.put(JWTClaim.IP, "0.127.0.0.1");
        claims.put(JWTClaim.ISSUER_SERVER, "AuthSphere");
        claims.put(JWTClaim.ROLE, "Roles");
        claims.put(JWTClaim.SESSION, "admin");

        return createToken(claims);
    }
}
