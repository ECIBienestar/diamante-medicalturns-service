package eci.cvds.ecibeneficio.diamante_medicalturns_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
  @Value("${PROD_SUPABASE_SERVICE_ROLE_KEY}")
  private String serviceRoleKey;

  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
        .defaultHeader("x-upsert", "true")
        .build();
  }
}
