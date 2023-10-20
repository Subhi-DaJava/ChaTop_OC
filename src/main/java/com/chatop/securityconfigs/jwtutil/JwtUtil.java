package com.chatop.securityconfigs.jwtutil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    //private final static String secretKey = "jwt";

    // explain text: JWT_TOKEN SecretKey: jwtToken_151023
    private final static String secretKey = "403a269d602cbad9664fd65a5576b1d18979f6e59e38fd69afff0c916ebc14c4";


    private final static Integer EXPIRED = 1000 * 60 * 60;

    public String extractUserEmail(String token) {
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
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String generateToken(String userEmail) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userEmail);
    }

    private String createToken(Map<String, Object> claims, String userEmail) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuer("ChâTop2023")
                .setSubject(userEmail)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRED))
                .signWith(getSignKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] secret = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(secret);
    }
}
