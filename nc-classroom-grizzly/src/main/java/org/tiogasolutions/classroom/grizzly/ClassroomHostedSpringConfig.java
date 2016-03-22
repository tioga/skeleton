package org.tiogasolutions.classroom.grizzly;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.tiogasolutions.classroom.engine.ClassroomApplication;
import org.tiogasolutions.classroom.engine.kernel.ClassroomCouchServer;
import org.tiogasolutions.classroom.engine.kernel.CouchServersConfig;
import org.tiogasolutions.classroom.engine.kernel.SessionStore;
import org.tiogasolutions.classroom.engine.readers.BundledStaticContentReader;
import org.tiogasolutions.classroom.jackson.ClassroomObjectMapper;
import org.tiogasolutions.dev.jackson.TiogaJacksonTranslator;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.GrizzlyServerConfig;

import java.util.concurrent.TimeUnit;

import static org.tiogasolutions.dev.common.EnvUtils.findProperty;

@Profile("hosted")
@Configuration
public class ClassroomHostedSpringConfig {

  @Bean
  public SessionStore sessionStore(@Value("${classroom.sessionDuration}") long sessionDuration) {
    return new SessionStore(sessionDuration);
  }

  @Bean
  BundledStaticContentReader bundledStaticContentReader() {
    return new BundledStaticContentReader("/org/tiogasolutions/classroom/admin/app");
  }

  @Bean
  public GrizzlyServerConfig grizzlyServerConfig(@Value("${classroom.hostName}") String hostName,
                                                 @Value("${classroom.port}") int port,
                                                 @Value("${classroom.shutdownPort}") int shutdownPort,
                                                 @Value("${classroom.context}") String context,
                                                 @Value("${classroom.toOpenBrowser}") boolean toOpenBrowser) {

    GrizzlyServerConfig config = new GrizzlyServerConfig();
    config.setHostName(hostName);
    config.setPort(port);
    config.setShutdownPort(shutdownPort);
    config.setContext(context);
    config.setToOpenBrowser(toOpenBrowser);
    return config;
  }

  @Bean
  public CouchServersConfig couchServersConfig(@Value("${classroom.couchUrl}") String url,
                                               @Value("${classroom.couchUsername}") String username,
                                               @Value("${classroom.couchPassword}") String password,
                                               @Value("${classroom.couchDatabaseName}") String databaseName) {

    return new CouchServersConfig(url, databaseName, username, password);
  }

  @Bean
  public ClassroomCouchServer classroomCouchServer(CouchServersConfig config) {
    return new ClassroomCouchServer(config);
  }

  @Bean
  public ClassroomApplication classroomApplication() {
    return new ClassroomApplication();
  }

  @Bean
  public ClassroomObjectMapper classroomObjectMapper() {
    return new ClassroomObjectMapper();
  }

  @Bean
  public TiogaJacksonTranslator tiogaJacksonTranslator(ClassroomObjectMapper classroomApplication) {
    return new TiogaJacksonTranslator(classroomApplication);
  }

  @Bean
  public GrizzlyServer grizzlyServer(GrizzlyServerConfig grizzlyServerConfig, ClassroomApplication application, ApplicationContext applicationContext) {

    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.property("contextConfig", applicationContext);
    resourceConfig.packages("org.tiogasolutions.classroom");

    return new GrizzlyServer(grizzlyServerConfig, resourceConfig);
  }
}
