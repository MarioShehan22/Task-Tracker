package com.tasktracker.security;

import com.tasktracker.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "tasktracker-tasktracker-tasktracker-tasktracker-tasktracker-2026-secret";

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // ================= GENERATE TOKEN =================
    public String generateToken(User user) {

        List<String> permissions = user.getRole()
                .getRolePermissions()
                .stream()
                .map(rp -> rp.getPermission().getPermissionName().name())
                .toList();

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole().getRoleName().name())
                .claim("permissions", permissions)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================= USERNAME =================
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // ================= ROLE =================
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    // ================= PERMISSIONS =================
    public List<String> extractPermissions(String token) {
        return extractAllClaims(token)
                .get("permissions", List.class);
    }

    // ================= VALIDATION =================
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(extractUsername(token))
                && !isTokenExpired(token);
    }

    // ================= EXPIRATION =================
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    // ================= CLAIM PARSER =================
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}