package org.tiogasolutions.classroom.engine.readers;

import javax.ws.rs.core.UriInfo;

public interface StaticContentReader {
  byte[] readContent(UriInfo uriInfo);
  byte[] readContent(String contentPath);
}
