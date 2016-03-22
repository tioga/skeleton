/*
 * Copyright (c) 2014 Jacob D. Parr
 *
 * This software may not be used without permission.
 */
package org.tiogasolutions.classroom.engine.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.tiogasolutions.classroom.engine.kernel.ExecutionManager;
import org.tiogasolutions.classroom.engine.writers.Thymeleaf;
import org.tiogasolutions.classroom.engine.writers.ThymeleafViewFactory;
import org.tiogasolutions.classroom.engine.kernel.SessionStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

@Path("/")
@Scope(value="prototype")
public class RootResource extends RootResourceSupport {

  public static final int REASON_CODE_INVALID_USERNAME_OR_PASSWORD = -1;
  public static final int REASON_CODE_UNAUTHORIZED = -2;
  public static final int REASON_SIGNED_OUT = -3;

  private static final Log log = LogFactory.getLog(RootResource.class);

  @Context
  private UriInfo uriInfo;

  @Autowired
  private ExecutionManager executionManager;

  @Autowired
  private SessionStore sessionStore;

  public RootResource() {
    log.info("Created ");
  }

  @Override
  public UriInfo getUriInfo() {
    return uriInfo;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public Thymeleaf getWelcome(@QueryParam("r") int reasonCode, @QueryParam("username") String username, @QueryParam("password") String password) throws IOException {

    String message = "";
    if (REASON_CODE_INVALID_USERNAME_OR_PASSWORD == reasonCode) {
      message = "Invalid username or password";
    } else if (REASON_CODE_UNAUTHORIZED == reasonCode) {
      message = "Your session has expired";
    } else if (REASON_SIGNED_OUT == reasonCode) {
      message = "You have successfully signed out";
    }

    return new Thymeleaf(executionManager.context().getSession(), ThymeleafViewFactory.WELCOME, new WelcomeModel(message));
  }

  public static class WelcomeModel {
    private final String message;
    public WelcomeModel(String message) {
      this.message = message;
    }
    public String getMessage() { return message; }
  }
}

