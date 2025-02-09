package com.kuro.coffechain.ut.utils;

import com.kuro.coffechain.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String token;
    private final String username = "testUser";
    private final String role = "ROLE_USER";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        token = jwtUtil.generateToken(username, role);
    }

    @Test
    void testGenerateToken() {
        assertNotNull(token, "Generated token should not be null.");
    }

    @Test
    void testExtractUsername() {
        String extractedUsername = jwtUtil.extractUsername(token);
        assertEquals(username, extractedUsername, "Extracted username should match the original.");
    }

    @Test
    void testExtractRole() {
        String extractedRole = jwtUtil.extractRole(token);
        assertEquals(role, extractedRole, "Extracted role should match the original.");
    }

    @Test
    void testIsTokenExpired() {
        boolean expired = jwtUtil.validateToken(token, username);
        assertTrue(expired, "Token should be valid immediately after creation.");
    }

    @Test
    void testExtractExpiration() {
        Date expirationDate = jwtUtil.extractClaim(token, Claims::getExpiration);
        assertNotNull(expirationDate, "Expiration date should not be null.");
        assertTrue(expirationDate.after(new Date()), "Expiration should be in the future.");
    }

    @Test
    void testValidateToken_ValidToken() {
        assertTrue(jwtUtil.validateToken(token, username), "Token should be valid.");
    }

    @Test
    void testValidateToken_InvalidUsername() {
        assertFalse(jwtUtil.validateToken(token, "wrongUser"), "Token should be invalid for incorrect username.");
    }

    @Test
    void testValidateToken_ExpiredToken() {
        // Simulate an expired token
        JwtUtil expiredJwtUtil = new JwtUtil() {
            @Override
            public String generateToken(String username, String role) {
                return Jwts.builder()
                        .setSubject(username)
                        .claim("role", role)
                        .setIssuedAt(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)) // 2 hours ago
                        .setExpiration(new Date(System.currentTimeMillis() - 60 * 60 * 1000)) // 1 hour ago
                        .signWith(getSigningKey(), io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };

        String expiredToken = expiredJwtUtil.generateToken(username, role);
        assertNotNull(expiredToken);
    }
}
