package eci.cvds.ecibeneficio.diamante_medicalturns_service.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

class SecurityConfigTest {
  @Test
  void testCorsConfigurationSource() {
    JwtAuthenticationFilter filter = mock(JwtAuthenticationFilter.class);
    SecurityConfig config = new SecurityConfig(filter);

    CorsConfigurationSource source = config.corsConfigurationSource();
    assertNotNull(source);

    var request = new org.springframework.mock.web.MockHttpServletRequest(null, "/api/test");
    CorsConfiguration corsConfig = source.getCorsConfiguration(request);
    assertNotNull(corsConfig);

    assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:3000"));
    assertTrue(corsConfig.getAllowedMethods().contains("*"));
    assertTrue(corsConfig.getAllowedHeaders().contains("*"));
  }
}
