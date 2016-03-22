package org.tiogasolutions.classroom.engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.tiogasolutions.lib.jaxrs.providers.TiogaReaderWriterProvider;
import org.tiogasolutions.classroom.jackson.ClassroomObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
@Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_HTML})
public class ClassroomReaderWriterProvider extends TiogaReaderWriterProvider {

  @Autowired
  public ClassroomReaderWriterProvider(ClassroomObjectMapper objectMapper) {
    super(objectMapper);
  }
}
