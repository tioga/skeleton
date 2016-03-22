package org.tiogasolutions.classroom.engine.kernel;

import javax.ws.rs.core.UriInfo;

public class ExecutionContext {

  private Session session;

  private final UriInfo uriInfo;

  public ExecutionContext(UriInfo uriInfo) {
    this.uriInfo = uriInfo;
  }

  public void setSession(Session session) {
    this.session = session;
  }

  public Session getSession() {
    return (session != null) ? session : new Session(-1, "dummy-session");
  }

  public UriInfo getUriInfo() {
    return uriInfo;
  }

  public void setLastMessage(String message) {
    if (session != null) {
      session.setLastMessage(message);
    }
  }
}
