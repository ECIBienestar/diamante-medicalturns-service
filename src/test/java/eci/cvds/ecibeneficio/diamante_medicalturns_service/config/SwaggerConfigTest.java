package eci.cvds.ecibeneficio.diamante_medicalturns_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    @Test
    void testCustomOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI openAPI = config.customOpenAPI();
        Info info = openAPI.getInfo();
        assertNotNull(openAPI);
        assertNotNull(info);
        assertEquals("Turnos medicos", info.getTitle());
        assertEquals("1.0", info.getVersion());
        assertEquals("Documentación de la API de Servicios de Bienestar Universitario", info.getDescription());
    }
}
