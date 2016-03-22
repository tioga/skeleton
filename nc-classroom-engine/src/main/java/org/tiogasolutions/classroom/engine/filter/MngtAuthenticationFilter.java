package org.tiogasolutions.classroom.engine.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.classroom.engine.kernel.ExecutionManager;
import org.tiogasolutions.classroom.engine.kernel.Session;
import org.tiogasolutions.classroom.engine.kernel.SessionStore;
import org.tiogasolutions.classroom.engine.resources.RootResource;
import org.tiogasolutions.dev.common.exceptions.ApiException;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;

@Provider
@MngtAuthentication
@Priority(Priorities.AUTHENTICATION + 1)
public class MngtAuthenticationFilter implements ContainerRequestFilter {

  private final SessionStore sessionStore;
  private final ExecutionManager executionManager;

  @Autowired
  public MngtAuthenticationFilter(ExecutionManager executionManager, SessionStore sessionStore) {
    this.sessionStore = sessionStore;
    this.executionManager = executionManager;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    try {
      Session session = sessionStore.getSession(requestContext);
      if (session == null) {
        throw ApiException.unauthorized();
      }

      String emailAddress = session.getEmailAddress();
      // Account account = accountStore.getByEmail(emailAddress);

//      if (account == null) {
//        throw ApiException.unauthorized();
//      }

      final SecurityContext securityContext = requestContext.getSecurityContext();
      requestContext.setSecurityContext(new MngtSecurityContext(securityContext, emailAddress));
      // executionManager.context().setAccount(account);

    } catch (ApiException e) {
      URI uri = requestContext.getUriInfo().getBaseUriBuilder().queryParam("r", RootResource.REASON_CODE_UNAUTHORIZED).build();
      Response response = Response.seeOther(uri).build();
      requestContext.abortWith(response);
    }
  }

  private static class MngtSecurityContext implements SecurityContext {
    private final boolean secure;
    private final String emailAddress;
    public MngtSecurityContext(SecurityContext securityContext, String emailAddress) {
      this.emailAddress = emailAddress;
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
        @Override
        public String getName() {
          return emailAddress;
        }
      };
    }
  }
}
