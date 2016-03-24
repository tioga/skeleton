package org.tiogasolutions.skeleton.engine;

import ch.qos.logback.classic.Level;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.SpringLifecycleListener;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.glassfish.jersey.test.JerseyTestNg;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.tiogasolutions.dev.common.LogbackUtils;

import javax.ws.rs.core.Application;

public class AbstractEngineJaxRsTest extends JerseyTestNg.ContainerPerClassTest {

  private ConfigurableListableBeanFactory beanFactory;

  @BeforeMethod
  public void autowireTest() throws Exception {
    beanFactory.autowireBean(this);
  }

  @Override
  protected Application configure() {
    LogbackUtils.initLogback(Level.WARN);

    AnnotationConfigApplicationContext applicationContext;

    applicationContext = new AnnotationConfigApplicationContext();
    applicationContext.getEnvironment().setActiveProfiles("test");
    applicationContext.scan("org.tiogasolutions.skeleton");
    applicationContext.refresh();

    // Inject our unit test with any beans.
    beanFactory = applicationContext.getBeanFactory();

    Application application = beanFactory.getBean(Application.class);

    ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
    resourceConfig.register(SpringLifecycleListener.class);
    resourceConfig.register(RequestContextFilter.class);
    resourceConfig.property("contextConfig", applicationContext);
    resourceConfig.packages("org.tiogasolutions.skeleton");

    return resourceConfig;
  }

  public String toHttpAuth(String username, String password) {
    return TestFactory.toHttpAuth(username, password);
  }
}
