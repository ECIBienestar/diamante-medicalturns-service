package eci.cvds.ecibeneficio.diamante_medicalturns_service.security;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String token = getToken(request);

    if (token == null || !jwtService.isTokenValid(token)) {
      filterChain.doFilter(request, response);
      return;
    }

    String email = jwtService.extractUserEmail(token);
    RoleEnum role = jwtService.extractRole(token);

    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(
            email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role.name())));

    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(auth);

    filterChain.doFilter(request, response);
  }

  private String getToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    return (authHeader != null && authHeader.startsWith("Bearer ")
        ? authHeader.substring(7)
        : null);
  }
}
