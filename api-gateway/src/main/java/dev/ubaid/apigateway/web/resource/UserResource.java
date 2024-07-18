package dev.ubaid.apigateway.web.resource;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/users")
public class UserResource {
    
    @GetMapping("current")
    public Mono<User> userInfo(@AuthenticationPrincipal OidcUser user) {
        return Mono.just(new User(user.getPreferredUsername()));
    }

    public record User(String name) {}
}

