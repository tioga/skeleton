package org.tiogasolutions.classroom.jackson;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tiogasolutions.dev.jackson.TiogaJacksonInjectable;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.dev.jackson.TiogaJacksonObjectMapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ClassroomObjectMapper extends TiogaJacksonObjectMapper {

  public ClassroomObjectMapper() {
    super(Arrays.asList(new TiogaJacksonModule(),
                        new ClassroomJacksonModule()),
        Collections.<TiogaJacksonInjectable>emptyList());
  }

  protected ClassroomObjectMapper(Collection<? extends Module> modules, Collection<? extends TiogaJacksonInjectable> injectables) {
    super(modules, injectables);
  }

  @Override
  public ObjectMapper copy() {
    _checkInvalidCopy(ClassroomObjectMapper.class);
    return new ClassroomObjectMapper(getModules(), getInjectables());
  }

}
