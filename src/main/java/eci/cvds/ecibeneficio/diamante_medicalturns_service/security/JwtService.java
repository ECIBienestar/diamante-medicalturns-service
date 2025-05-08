package eci.cvds.ecibeneficio.diamante_medicalturns_service.security;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
  @Value("${SECRET_KEY}")
  private String sercretKey;

  public String extractUserEmail(String token) {
    return extractClaims(token, Claims::getSubject);
  }

  public RoleEnum extractRole(String token) {
    String role = extractClaims(token, claims -> claims.get("role", String.class));

    return RoleEnum.valueOf(role);
  }

  public boolean isTokenValid(String token) {
    try {
      extractAllClaims(token);
      return !isTokenExpired(token);
    } catch (Exception e) {
      return false;
    }
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
  }

  private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Date extractExpiration(String token) {
    return extractClaims(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private SecretKey getKey() {
    byte[] keyBytes = Decoders.BASE64.decode(sercretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
