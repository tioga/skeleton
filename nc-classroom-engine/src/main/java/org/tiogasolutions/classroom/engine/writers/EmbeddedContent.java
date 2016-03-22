package org.tiogasolutions.classroom.engine.writers;

import javax.ws.rs.core.UriInfo;

public class EmbeddedContent {

  private final String view;

  public EmbeddedContent(String view) {
    this.view = view;
  }

  public EmbeddedContent(UriInfo uriInfo) {
    this("/"+uriInfo.getPath());
  }

  public String getView() {
    return view;
  }
}
