package eci.cvds.ecibeneficio.diamante_medicalturns_service.jwt;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;
    private SecretKey secretKey;
    private String token;
    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        try {
            var secretKeyField = JwtService.class.getDeclaredField("secretKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(jwtService, base64Secret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of("ADMINISTRATOR"));
        token = Jwts.builder()
                .setClaims(claims)
                .setSubject("testuser@example.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(secretKey)
                .compact();
    }

    @Test
    void testExtractUserEmail() {
        String email = jwtService.extractUserEmail(token);
        assertEquals("testuser@example.com", email);
    }

    @Test
    void testExtractRole() {
        RoleEnum role = jwtService.extractRole(token);
        assertEquals(RoleEnum.ADMINISTRATOR, role);
    }

    @Test
    void testIsTokenValid() {
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void testExpiredTokenIsInvalid() {
        String expiredToken = Jwts.builder()
                .setSubject("expired@example.com")
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60))
                .signWith(secretKey)
                .compact();

        assertFalse(jwtService.isTokenValid(expiredToken));
    }

}
