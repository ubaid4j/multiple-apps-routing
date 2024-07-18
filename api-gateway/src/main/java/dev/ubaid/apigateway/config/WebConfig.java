package dev.ubaid.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.DefaultWebSessionManager;
import org.springframework.web.server.session.WebSessionManager;

@Configuration
public class WebConfig {

    /**
     * @param domain: domain name
     * @return return a web session manager that add the domain in the cookie so that we can use SESSION cookie across subdomains
     */
//    @Bean
    WebSessionManager webSessionManager(@Value("${application.domain}") String domain) {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        CookieWebSessionIdResolver webSessionIdResolver = (CookieWebSessionIdResolver) webSessionManager.getSessionIdResolver();
        webSessionIdResolver.addCookieInitializer(builder -> builder.domain(domain));
        return webSessionManager;
    }
}
