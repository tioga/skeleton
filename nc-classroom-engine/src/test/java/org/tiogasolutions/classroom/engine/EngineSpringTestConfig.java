package org.tiogasolutions.classroom.engine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.tiogasolutions.classroom.engine.kernel.ClassroomCouchServer;
import org.tiogasolutions.classroom.engine.kernel.CouchServersConfig;
import org.tiogasolutions.classroom.engine.readers.MockContentReader;
import org.tiogasolutions.classroom.jackson.ClassroomObjectMapper;

@Profile("test")
@Configuration
@PropertySource("classpath:/nc-classroom-engine/spring-test.properties")
public class EngineSpringTestConfig {

  @Bean
  public ClassroomCouchServer classroomCouchServer(CouchServersConfig config) {
    return new ClassroomCouchServer(config);
  }

  @Bean
  public ClassroomObjectMapper classroomObjectMapper() {
    return new ClassroomObjectMapper();
  }

  @Bean
  public MockContentReader mockContentReader() {
    return new MockContentReader();
  }

  @Bean
  ClassroomApplication classroomApplication() {
    return new ClassroomApplication();
  }

  @Bean
  public CouchServersConfig couchServersConfig() {
    return new CouchServersConfig("http://localhost:5984",
                                  "test-classroom",
                                  "test-user",
                                  "test-user");
  }
}
