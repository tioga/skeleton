package org.tiogasolutions.classroom.engine.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.tiogasolutions.classroom.engine.AbstractEngineJaxRsTest;
import org.tiogasolutions.classroom.engine.kernel.ExecutionManager;
import org.tiogasolutions.classroom.jackson.ClassroomObjectMapper;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;

@Test
public class RootResourceTest extends AbstractEngineJaxRsTest {

  @Autowired
  private ExecutionManager executionManager;

  @Autowired
  private ClassroomObjectMapper objectMapper;

  @BeforeMethod
  public void beforeClass() throws Exception {
  }

  @AfterMethod
  public void afterClass() throws Exception {
  }

  private Invocation.Builder request(WebTarget webTarget) {
    return webTarget.request().header("Authorization", toHttpAuth("admin", "Testing123"));
  }
  
  public void someTest() {
  }
}