package dev.ubaid.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> AUTHORIZE_EXCHANGE = (spec) -> spec
            .pathMatchers(HttpMethod.GET, "/login/**", "/favicon.ico", "/exception").permitAll()
            .anyExchange()
            .authenticated();

    private static final Customizer<ServerHttpSecurity.ExceptionHandlingSpec> EXCEPTION_HANDLING = (spec) -> spec
            .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/customer1"));


    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(AUTHORIZE_EXCHANGE)
                .oauth2Login(Customizer.withDefaults())
                .exceptionHandling(EXCEPTION_HANDLING)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
}
