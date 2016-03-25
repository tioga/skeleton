package org.tiogasolutions.skeleton.engine.mock;

import org.tiogasolutions.app.standard.jaxrs.auth.StandardAuthenticationResponseFactory;
import org.tiogasolutions.app.standard.session.SessionStore;
import org.tiogasolutions.app.standard.view.thymeleaf.ThymeleafContent;
import org.tiogasolutions.skeleton.engine.resources.RootResource;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

public class SkeletonAuthenticationResponseFactory implements StandardAuthenticationResponseFactory {

    private final AccountStore accountStore;
    private final SessionStore sessionStore;

    public SkeletonAuthenticationResponseFactory(AccountStore accountStore, SessionStore sessionStore) {
        this.accountStore = accountStore;
        this.sessionStore = sessionStore;
    }

    @Override
    public Response createForbiddenResponse(ContainerRequestContext requestContext) {
        String msg = "You do not have permission to access this page.";
        RootResource.IndexModel indexModel = new RootResource.IndexModel(msg, accountStore.getAll());
        ThymeleafContent content = new ThymeleafContent("forbidden", indexModel);
        Response.ResponseBuilder builder = Response.status(401).entity(content );

        Cookie cookie = getSessionCookie(requestContext);
        if (cookie != null) {
            sessionStore.remove(cookie);
            NewCookie deleteSessionCookie = new NewCookie(cookie, null, 0, true);
            builder.cookie(deleteSessionCookie);
        }

        return builder.build();
    }

    @Override
    public Response createUnauthorizedResponse(ContainerRequestContext requestContext) {
        String msg = "Invalid username or password";
        RootResource.IndexModel indexModel = new RootResource.IndexModel(msg, accountStore.getAll());
        ThymeleafContent content = new ThymeleafContent("index", indexModel);
        Response.ResponseBuilder builder = Response.status(401).entity(content );

        Cookie cookie = getSessionCookie(requestContext);
        if (cookie != null) {
            sessionStore.remove(cookie);
            NewCookie deleteSessionCookie = new NewCookie(cookie, null, 0, true);
            builder.cookie(deleteSessionCookie);
        }

        return builder.build();
    }

    private Cookie getSessionCookie(ContainerRequestContext requestContext) {
        String cookieName = sessionStore.getCookieName();
        return requestContext.getCookies().get(cookieName);
    }
}
