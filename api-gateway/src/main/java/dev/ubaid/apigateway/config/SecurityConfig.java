package dev.ubaid.apigateway.config;

import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
@RegisterReflectionForBinding(classes = {
        OAuth2AuthorizationRequest.class,
        ClientRegistration.class,
        OAuth2AccessToken.class,
        OAuth2RefreshToken.class,
        OAuth2AuthorizedClient.class,
        OAuth2UserAuthority.class,
        DefaultOAuth2User.class,
        OidcIdToken.class,
        OidcUserInfo.class,
        OidcUserAuthority.class,
        DefaultOidcUser.class,
        OAuth2AuthenticationToken.class,
        OAuth2AuthenticationException.class,
        OAuth2Error.class
})
public class SecurityConfig {
    
    private static final String[] PERMITTED_PATH = {
            "favicon.ico", "/management/**"
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
