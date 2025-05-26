package eci.cvds.ecibeneficio.diamante_medicalturns_service.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtAuthenticationFilter;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.security.JwtService;
import eci.cvds.ecibeneficio.diamante_medicalturns_service.utils.enums.RoleEnum;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueFilterIfNoTokenPresent() throws ServletException, IOException {
        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldNotAuthenticateIfTokenIsInvalid() throws ServletException, IOException {
        String token = "invalid_token";
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtService.isTokenValid(token)).thenReturn(false);
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldAuthenticateIfTokenIsValid() throws ServletException, IOException {
        String token = "valid_token";
        String email = "test@example.com";
        RoleEnum role = RoleEnum.MEDICAL_STAFF;
        request.addHeader("Authorization", "Bearer " + token);
        when(jwtService.isTokenValid(token)).thenReturn(true);
        when(jwtService.extractUserEmail(token)).thenReturn(email);
        when(jwtService.extractRole(token)).thenReturn(role);
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
        assertEquals(email, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }

    @Test
    void shouldSkipFilterWhenNoAuthorizationHeader() throws Exception {
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldSkipFilterWhenAuthorizationHeaderDoesNotStartWithBearer() throws Exception {
        request.addHeader("Authorization", "Token abc.def.ghi"); // mal formado
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }


}

