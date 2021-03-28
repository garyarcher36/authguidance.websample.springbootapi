package com.mycompany.sample.host.controllers;

import java.util.concurrent.CompletableFuture;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mycompany.sample.logic.entities.ClientUserInfo;
import com.mycompany.sample.plumbing.claims.BaseClaims;
import com.mycompany.sample.plumbing.claims.UserInfoClaims;
import com.mycompany.sample.plumbing.dependencies.CustomRequestScope;

/*
 * A simple controller to return user info to the caller
 */
@RestController
@Scope(value = CustomRequestScope.NAME)
@RequestMapping(value = "api/userinfo")
@SuppressWarnings(value = "checkstyle:DesignForExtension")
public class UserInfoController {

    private final BaseClaims _baseClaims;
    private final UserInfoClaims _userInfoClaims;

    /*
     * The claims resolver is injected into the controller after OAuth processing
     */
    public UserInfoController(final BaseClaims baseClaims, final UserInfoClaims userInfoClaims) {
        this._baseClaims = baseClaims;
        this._userInfoClaims = userInfoClaims;
    }

    /*
     * Return the user info claims when the API is called
     */
    @GetMapping(value = "")
    public CompletableFuture<ClientUserInfo> getUserClaims() {

        // First check scopes
        this._baseClaims.verifyScope("profile");

        // Next return the user info
        var userInfo = new ClientUserInfo();
        userInfo.setGivenName(this._userInfoClaims.get_givenName());
        userInfo.setFamilyName(this._userInfoClaims.get_familyName());
        return CompletableFuture.completedFuture(userInfo);
    }
}
