package com.example.demo.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Jwt {
    private static final String SECRET = "meeting_room_secret_key_2026";
    private static final long EXPIRE = 7 * 24 * 60 * 60 * 1000;
    public static String generateToken(Long userId,Integer role) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + EXPIRE);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role",role)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(SignatureAlgorithm.HS256,SECRET)
                .compact();
    }
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.parseLong(claims.getSubject());
    }
    public static Integer getRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", Integer.class);
    }
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
