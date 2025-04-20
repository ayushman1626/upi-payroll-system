package com.upipaysystem.payroll.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    private static final long EXPIRATION_TIME = 10 * 60 * 60 * 1000;
    private static final String SECRET_KEY = "RxDcyO93MqM4i/K+0DyA0kw4Z4S1plizP8MJTGbzsk+Q8QV6SZT/DIFRpqmYUA78fJY=";

    public String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(email)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(getkey(), Jwts.SIG.HS256)
                    .compact();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private SecretKey getkey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Jwts.parser()
                    .verifyWith(getkey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }



    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (claims != null) {
            return claimsResolver.apply(claims);
        }
        return null;
    }


    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getkey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e) {
            return null;
        }
    }


}

