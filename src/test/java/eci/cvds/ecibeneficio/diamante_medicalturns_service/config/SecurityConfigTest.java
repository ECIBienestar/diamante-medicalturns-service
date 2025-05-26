package eci.cvds.ecibeneficio.diamante_medicalturns_service.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@WebMvcTest(SecurityConfig.class)
class SecurityConfigTest {
  @Autowired private MockMvc mockMvc;

  @MockitoBean private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Test
  void shouldAllowAccessToSwaggerWithoutAuthentication() throws Exception {
    mockMvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
  }

  @Test
  void testCorsConfigurationSource() {
    SecurityConfig config = new SecurityConfig(jwtAuthenticationFilter);

    CorsConfigurationSource source = config.corsConfigurationSource();
    assertNotNull(source);

    var request = new MockHttpServletRequest(null, "/api/test");
    CorsConfiguration corsConfig = source.getCorsConfiguration(request);
    assertNotNull(corsConfig);

    assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:3000"));
    assertTrue(corsConfig.getAllowedMethods().contains("*"));
    assertTrue(corsConfig.getAllowedHeaders().contains("*"));
  }
}
