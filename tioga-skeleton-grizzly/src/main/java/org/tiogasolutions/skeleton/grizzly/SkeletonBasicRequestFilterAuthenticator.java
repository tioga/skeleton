package org.tiogasolutions.skeleton.grizzly;

import org.tiogasolutions.app.standard.jaxrs.auth.BasicRequestFilterAuthenticator;
import org.tiogasolutions.dev.common.BeanUtils;
import org.tiogasolutions.dev.common.EqualsUtils;
import org.tiogasolutions.skeleton.engine.mock.SkeletonAuthenticationResponseFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public class SkeletonBasicRequestFilterAuthenticator extends BasicRequestFilterAuthenticator {

    private final Map<String,String> trusted = new HashMap<>();
    private SkeletonAuthenticationResponseFactory authenticationResponseFactory;

    public SkeletonBasicRequestFilterAuthenticator(SkeletonAuthenticationResponseFactory authenticationResponseFactory, String firstTrusted, String...otherTrusted) {
        this.trusted.putAll(BeanUtils.toMap(firstTrusted));
        this.trusted.putAll(BeanUtils.toMap(otherTrusted));
        this.authenticationResponseFactory = authenticationResponseFactory;
    }

    @Override
    protected SecurityContext validate(ContainerRequestContext requestContext, String username, String password) {

        if (trusted.containsKey(username) == false) {
            authenticationResponseFactory.createUnauthorizedResponse(requestContext);

        } else if (EqualsUtils.objectsNotEqual(trusted.get(username), password)) {
            authenticationResponseFactory.createUnauthorizedResponse(requestContext);

        }
        return new ApiBasedSecurityContext(requestContext.getSecurityContext(), username);
    }

    private static class ApiBasedSecurityContext implements SecurityContext {
        private final boolean secure;
        private final String username;
        public ApiBasedSecurityContext(SecurityContext securityContext, String username) {
            this.username = username;
            this.secure = securityContext.isSecure();
        }
        @Override public boolean isUserInRole(String role) {
            return false;
        }
        @Override public boolean isSecure() {
            return secure;
        }
        @Override public String getAuthenticationScheme() {
            return "FORM_AUTH";
        }
        @Override public Principal getUserPrincipal() {
            return new Principal() {
                @Override public String getName() { return username; }
            };
        }
    }
}
