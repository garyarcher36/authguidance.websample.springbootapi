package com.mycompany.api.basicapi.entities;

import com.mycompany.api.basicapi.entities.BasicApiClaims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/*
 * A helper to enable claims to be injected, which could be abstracted behind an interface
 */
@Component
@RequestScope
public class BasicApiClaimsProvider {

    /*
     * Return the API claims set by the security handler
     */
    public BasicApiClaims getApiClaims() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            var principal = authentication.getPrincipal();
            if(principal instanceof BasicApiClaims) {
                return (BasicApiClaims)principal;
            }
        }

        throw new RuntimeException("Unable to retrieve API claims from the security context");
    }
}
