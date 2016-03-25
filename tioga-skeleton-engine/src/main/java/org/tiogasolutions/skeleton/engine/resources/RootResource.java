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
import org.tiogasolutions.app.standard.readers.StaticContentReader;
import org.tiogasolutions.app.standard.session.Session;
import org.tiogasolutions.app.standard.session.SessionStore;
import org.tiogasolutions.app.standard.view.thymeleaf.ThymeleafContent;
import org.tiogasolutions.dev.common.EqualsUtils;
import org.tiogasolutions.dev.common.net.InetMediaType;
import org.tiogasolutions.skeleton.engine.mock.Account;
import org.tiogasolutions.skeleton.engine.mock.AccountStore;
import org.tiogasolutions.skeleton.engine.mock.SkeletonAuthenticationResponseFactory;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Path("/")
@Scope(value = "prototype")
public class RootResource extends RootResourceSupport {

    private static final Log log = LogFactory.getLog(RootResource.class);

    @Context
    private UriInfo uriInfo;

    @Context
    private ContainerRequestContext requestContext;

    @Autowired
    private SessionStore sessionStore;

    @Autowired
    private AccountStore accountStore;

    @Autowired
    private StandardRequestFilterConfig standardRequestFilterConfig;

    @Autowired
    private StaticContentReader staticContentReader;

    @Autowired
    private SkeletonAuthenticationResponseFactory authenticationResponseFactory;

    public RootResource() {
        log.info("Created ");
    }

    @Override
    public UriInfo getUriInfo() {
        return uriInfo;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public ThymeleafContent getIndex() throws IOException {
        IndexModel indexModel = new IndexModel(null, accountStore.getAll());
        return new ThymeleafContent("index", indexModel);
    }

    @GET
    @Path("/welcome")
    @Produces(MediaType.TEXT_HTML)
    public ThymeleafContent getWelcome() throws IOException {
        return new ThymeleafContent("welcome", null);
    }

    @POST
    @Path("/sign-in")
    @Produces(MediaType.TEXT_HTML)
    public Response signIn(@FormParam("email") String email, @FormParam("password") String password) throws Exception {
        Account account = accountStore.findByEmail(email);

        if (account == null || EqualsUtils.objectsNotEqual(account.getPassword(), password)) {
            return authenticationResponseFactory.createUnauthorizedResponse(requestContext, SecurityContext.FORM_AUTH);
        }

        // Create the new session for the currently logged in user.
        Session session = sessionStore.newSession();
        session.put("account", account);

        NewCookie sessionCookie = sessionStore.newSessionCookie(session, uriInfo);
        URI other = getUriInfo().getBaseUriBuilder().path("welcome").build();
        return Response.seeOther(other).cookie(sessionCookie).build();
    }

    @GET
    @Produces(InetMediaType.APPLICATION_JSON_VALUE)
    @Path("/api")
    public Account getApi() throws Exception {
        return accountStore.findByEmail("mickey.mouse@disney.com");
    }

    private Cookie getSessionCookie() {
        String cookieName = sessionStore.getCookieName();
        return requestContext.getCookies().get(cookieName);
    }

    public static class IndexModel {
        private final String message;
        private final List<Account> accounts = new ArrayList<>();

        public IndexModel(String message, Collection<Account> accounts) {
            this.message = message;
            this.accounts.addAll(accounts);
        }
        public String getMessage() {
            return message;
        }
        public List<Account> getAccounts() { return accounts; }
    }
}

