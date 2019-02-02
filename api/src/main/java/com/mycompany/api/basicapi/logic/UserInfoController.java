package com.mycompany.api.basicapi.logic;

import com.mycompany.api.basicapi.entities.BasicApiClaims;
import com.mycompany.api.basicapi.entities.BasicApiClaimsProvider;
import com.mycompany.api.basicapi.plumbing.oauth.UserInfoClaims;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import java.util.concurrent.CompletableFuture;

/*
 * For fewer thread safety risks we create the controller class on every API request
 * We implement async according to the below post
 *
 */
@RestController
@RequestMapping(value = "api/userclaims")
@RequestScope
public class UserInfoController {

    /*
     * Claims are injected
     */
    private final BasicApiClaims claims;

    /*
     * Receive claims from the security context
     */
    public UserInfoController(BasicApiClaimsProvider claimsProvider) {
        this.claims = claimsProvider.getApiClaims();
    }

    /*
     * Return the user info claims when the API is called
     */
    @GetMapping(value="current")
    public CompletableFuture<UserInfoClaims> getUserClaims()
    {
        return CompletableFuture.completedFuture(this.claims.getCentralUserInfo());
    }
}
