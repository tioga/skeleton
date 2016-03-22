package org.tiogasolutions.classroom.engine.writers;

import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Component
public class EmbeddedContentMessageBodyWriter implements MessageBodyWriter<EmbeddedContent> {

  @Context UriInfo uriInfo;

  // HACK - if we want to go this way will need to uncomment this!
  //@Autowired // Injected by CDI, not Spring
  //private StaticContentReader contentReader;

  public EmbeddedContentMessageBodyWriter() {
  }

  @Override
  public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return EmbeddedContent.class.equals(type);
  }

  @Override
  public long getSize(EmbeddedContent embeddedContent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
    return -1;
  }

  @Override
  public void writeTo(EmbeddedContent embeddedContent, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {

    //byte[] bytes = contentReader.readContent(embeddedContent.getView());
    //entityStream.write(bytes);
  }
}
