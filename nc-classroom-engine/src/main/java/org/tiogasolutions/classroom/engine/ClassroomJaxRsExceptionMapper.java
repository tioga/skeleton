package org.tiogasolutions.classroom.engine;

import org.tiogasolutions.lib.jaxrs.providers.TiogaJaxRsExceptionMapper;

import javax.ws.rs.ext.Provider;

@Provider
public class ClassroomJaxRsExceptionMapper extends TiogaJaxRsExceptionMapper {

  public ClassroomJaxRsExceptionMapper() {
  }

}
