
package com.ironforge.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    private final String SECRET = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQlJFIiwic3ViIjoiTWFydGluMDFAaXJvbmZvcmdlLmNvbSIsImlhdCI6MTc3NzMwOTUzMCwiZXhwIjoxNzc3Mzk1OTMwfQ.aRFQLiDfXLCBNY7_w6xAZ1isB8UJPTvOWet1Ta0g1Zk";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✅ EXTRAIRE EMAIL (USERNAME)
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 🔍 Lire toutes les infos du token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
