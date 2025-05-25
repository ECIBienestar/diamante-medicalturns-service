package eci.cvds.ecibeneficio.diamante_medicalturns_service.config;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    @Test
    void testCorsConfigurationSource() {
        JwtAuthenticationFilter filter = mock(JwtAuthenticationFilter.class);
        SecurityConfig config = new SecurityConfig(filter);

        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source);
        var corsConfig = source.getCorsConfiguration(new org.springframework.mock.web.MockHttpServletRequest(null, "/api/test"));
        assertNotNull(corsConfig);
        assertTrue(corsConfig.getAllowedOrigins().contains("http://localhost:5173"));
    }

    @Test
    void testWebMvcConfigurer() {
        JwtAuthenticationFilter filter = mock(JwtAuthenticationFilter.class);
        SecurityConfig config = new SecurityConfig(filter);

        WebMvcConfigurer webMvcConfigurer = config.corsConfigurer();
        assertNotNull(webMvcConfigurer);
    }
}
