package org.tiogasolutions.classroom.engine.kernel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tiogasolutions.classroom.jackson.ClassroomJacksonModule;
import org.tiogasolutions.couchace.core.api.CouchSetup;
import org.tiogasolutions.couchace.jackson.JacksonCouchJsonStrategy;
import org.tiogasolutions.couchace.jersey.JerseyCouchHttpClient;
import org.tiogasolutions.dev.jackson.TiogaJacksonModule;
import org.tiogasolutions.lib.couchace.DefaultCouchServer;

public class ClassroomCouchServer extends DefaultCouchServer {

  @Autowired
  public ClassroomCouchServer(CouchServersConfig config) {
    super(createCouchSetup(config));
  }

  public static CouchSetup createCouchSetup(CouchServersConfig config) {
    CouchSetup setup = new CouchSetup(config.getUrl());
    setup.setUserName(config.getUsername());
    setup.setPassword(config.getPassword());
    setup.setHttpClient(JerseyCouchHttpClient.class);
    setup.setJsonStrategy(new JacksonCouchJsonStrategy(
      new TiogaJacksonModule(),
      new ClassroomJacksonModule()
    ));
    return setup;
  }
}
