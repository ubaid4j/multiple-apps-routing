package dev.ubaid.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    private static final String[] PERMITTED_PATH = {
            "favicon.ico"
    };
    
    private static final Customizer<ServerHttpSecurity.AuthorizeExchangeSpec> AUTHORIZE_EXCHANGE = (spec) -> spec
            .pathMatchers(HttpMethod.GET, PERMITTED_PATH).permitAll()
            .anyExchange()
            .authenticated();

    private static final Customizer<ServerHttpSecurity.ExceptionHandlingSpec> EXCEPTION_HANDLING = (spec) -> spec
            .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/customer1"));

    
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }
    
    

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, ServerLogoutSuccessHandler oidcLogoutSuccessHandler) {
        return http
                .authorizeExchange(AUTHORIZE_EXCHANGE)
                .oauth2Login(Customizer.withDefaults())
                .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler))
                .exceptionHandling(EXCEPTION_HANDLING)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .build();
    }
    
    @Bean
    ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedServerLogoutSuccessHandler successHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("{baseScheme}://{baseHost}{basePort}/{basePath}");
        return successHandler;
    }
}
