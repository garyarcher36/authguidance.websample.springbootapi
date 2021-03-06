package com.mycompany.sample.plumbing.utilities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.mycompany.sample.plumbing.claims.ApiClaims;
import com.mycompany.sample.plumbing.claims.UserInfoClaims;
import com.mycompany.sample.plumbing.dependencies.CustomRequestScope;

/*
 * Spring DI creates request objects before authentication begins so we inject a resolver
 * This gives us time to do authentication work before the below getClaims method is called
 */
@Component
@Scope(value = CustomRequestScope.NAME)
@SuppressWarnings(value = "checkstyle:DesignForExtension")
public class UserInfoClaimsResolver {

    /*
     * Resolve the claims object when getClaims is asked for
     */
    @Bean
    @Scope(value = CustomRequestScope.NAME)
    public UserInfoClaims getClaims() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return ((ApiClaims) authentication.getPrincipal()).getUserInfo();
        }

        return null;
    }
}