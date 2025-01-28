package rw.cozy.cozybackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rw.cozy.cozybackend.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final SecretKey key;
    private final int expiration;

    public JwtService(
            @Value("${jwt.jwtSecret}") String jwtSecret,
            @Value("${jwt.expiration}") int expiration) {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        byte[] paddedKey = Arrays.copyOf(keyBytes, 64);
        log.debug("Auth Service Key Bytes (first 10): {}",
                Arrays.toString(Arrays.copyOfRange(paddedKey, 0, 10)));
        this.key = Keys.hmacShaKeyFor(paddedKey);
        this.expiration = expiration;
    }

    public String generateToken(String userName, UUID userId, String role, User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", userName);
        claims.put("userId", userId);
        claims.put("role", role);
        claims.put("authorities",user.getAuthorities());

        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();

        log.debug("Generated Token (first 20 chars): {}",
                token.substring(0, Math.min(token.length(), 20)) + "...");
        return token;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
            log.debug("Token validation result for user {}: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            return false;
        }
    }
}