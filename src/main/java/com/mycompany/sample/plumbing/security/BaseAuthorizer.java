package com.mycompany.sample.plumbing.security;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import com.mycompany.sample.plumbing.claims.CoreApiClaims;
import com.mycompany.sample.plumbing.interceptors.UnhandledExceptionHandler;
import com.mycompany.sample.plumbing.logging.LogEntryImpl;
import com.mycompany.sample.plumbing.utilities.RequestClassifier;
import lombok.AccessLevel;
import lombok.Getter;

/*
 * Base authorizer logic related to wiring up Spring Boot, error responses and identity logging
 */
@SuppressWarnings(value = "checkstyle:DesignForExtension")
public abstract class BaseAuthorizer extends OncePerRequestFilter {

    @Getter(AccessLevel.PROTECTED)
    private final BeanFactory container;

    public BaseAuthorizer(final BeanFactory container) {
        this.container = container;
    }

    /*
     * We override entire security processing since our solution is based on our Authorizer pattern
     * This also provides complete control over all error responses, since Spring defaults can be poor
     */
    @Override
    protected void doFilterInternal(
        final @NonNull HttpServletRequest request,
        final @NonNull HttpServletResponse response,
        final @NonNull FilterChain filterChain) {

        try {

            // Make sure we only process real API requests
            var requestClassifier = this.container.getBean(RequestClassifier.class);
            if (!requestClassifier.isApiRequest(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // For secured requests, API logging starts here
            var handlerMappings = this.container.getBean(RequestMappingHandlerMapping.class);
            var logEntry = this.container.getBean(LogEntryImpl.class);
            logEntry.start(request, handlerMappings);

            // Do the authorization work via the concrete class
            System.out.println("*** CALLING AUTHORIZER");
            var claims = this.execute(request);

            // Log who called the API
            logEntry.setIdentity(claims);

            // Update the Spring security context, then move onto business logic
            SecurityContextHolder.getContext().setAuthentication(new SpringAuthentication(claims));
            System.out.println("*** PRINCIPAL IS SET");
            filterChain.doFilter(request, response);

        } catch (Throwable ex) {

            // Ensure that authorization errors return the correct exception and CORS details
            var handler = this.container.getBean(UnhandledExceptionHandler.class);
            handler.handleFilterException(request, response, ex);
        }
    }

    // Concrete classes must override this
    protected abstract CoreApiClaims execute(HttpServletRequest request);
}
