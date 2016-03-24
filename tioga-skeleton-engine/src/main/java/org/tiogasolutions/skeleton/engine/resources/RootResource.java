/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.skeleton.engine.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.tiogasolutions.app.standard.jaxrs.filters.StandardRequestFilterConfig;
import org.tiogasolutions.app.standard.session.Session;
import org.tiogasolutions.app.standard.session.SessionStore;
import org.tiogasolutions.app.standard.view.thymeleaf.ThymeleafContent;
import org.tiogasolutions.dev.common.EqualsUtils;
import org.tiogasolutions.skeleton.engine.mock.Account;
import org.tiogasolutions.skeleton.engine.mock.AccountStore;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;

@Path("/")
@Scope(value = "prototype")
public class RootResource extends RootResourceSupport {

    public static final String REASON_CODE_QUERY_PARAM_NAME = "r";
    public static final String REASON_CODE_UNAUTHORIZED_QUERY_PARAM_VALUE = "UNAUTHORIZED";
    public static final String REASON_CODE_INVALID_USERNAME_OR_PASSWORD = "INVALID";
    public static final String REASON_SIGNED_OUT = "OUT";

    private static final Log log = LogFactory.getLog(RootResource.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private Cookie[] cookies = new Cookie[0];

    @Context
    private ContainerRequestContext requestContext;

    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private AccountStore accountStore;

    @Autowired
    private StandardRequestFilterConfig standardRequestFilterConfig;

    public RootResource() {
        log.info("Created ");
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ThymeleafContent getIndex(@QueryParam(REASON_CODE_QUERY_PARAM_NAME) String reasonCode,
                                     @QueryParam("email") String email,
                                     @QueryParam("password") String password) throws IOException {

        String message = "";
        if (REASON_CODE_INVALID_USERNAME_OR_PASSWORD == reasonCode) {
            message = "Invalid username or password";
        } else if (REASON_CODE_UNAUTHORIZED_QUERY_PARAM_VALUE == reasonCode) {
            message = "Your session has expired";
        } else if (REASON_SIGNED_OUT == reasonCode) {
            message = "You have successfully signed out";
        }

        return new ThymeleafContent("index", new IndexModel(message));
    }

    @POST
    @Path("/sign-in")
    @Produces(MediaType.TEXT_HTML)
    public Response signIn(@FormParam("email") String email, @FormParam("password") String password) throws Exception {
        Cookie cookie = getSessionCookie();

        Account account = accountStore.findByEmail(email);

        if (account == null || EqualsUtils.objectsNotEqual(account.getPassword(), password)) {
            sessionStore.remove(cookie);
            NewCookie deleteSessionCookie = sessionStore.newSessionCookie(requestContext);

            URI other = getUriInfo()
                    .getBaseUriBuilder()
                    .path(standardRequestFilterConfig.getUnauthorizedPath())
                    .queryParam(REASON_CODE_QUERY_PARAM_NAME, REASON_CODE_INVALID_USERNAME_OR_PASSWORD)
                    .build();

            return Response.seeOther(other).cookie(deleteSessionCookie).build();
        }

        // Create the new session for the currently logged in user.
        Session session = sessionStore.newSession();
        session.put("account", account);

        NewCookie sessionCookie = sessionStore.newSessionCookie(requestContext);
        URI other = getUriInfo().getBaseUriBuilder().path("welcome").build();
        return Response.seeOther(other).cookie(sessionCookie).build();
    }

    private Cookie getSessionCookie() {
        for (Cookie cookie : cookies) {
            if (EqualsUtils.objectsEqual(cookie.getName(), sessionStore.getCookieName())) {
                return cookie;
            }
        }
        return null;
    }

    public static class IndexModel {
        private final String message;

        public IndexModel(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

