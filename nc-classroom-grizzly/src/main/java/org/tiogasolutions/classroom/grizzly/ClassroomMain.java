package org.tiogasolutions.classroom.grizzly;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.tiogasolutions.app.common.AppPathResolver;
import org.tiogasolutions.app.common.AppUtils;
import org.tiogasolutions.lib.spring.SpringUtils;
import org.tiogasolutions.runners.grizzly.GrizzlyServer;
import org.tiogasolutions.runners.grizzly.ShutdownUtils;

import java.nio.file.Path;
import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.slf4j.LoggerFactory.getLogger;

public class ClassroomMain {

  private static final Logger log = getLogger(ClassroomMain.class);

  public static void main(String...args) throws Exception {
    // Priority #1, configure default logging levels. This will be
    // overridden later when/if the logback.xml is found and loaded.
    AppUtils.initLogback(Level.WARN);

    // Assume we want by default INFO on when & how the grizzly server
    // is started. Possibly overwritten by logback.xml if used.
    AppUtils.setLogLevel(Level.INFO, ClassroomMain.class);
    AppUtils.setLogLevel(Level.INFO, GrizzlyServer.class);

    // Load the resolver which gives us common tools for identifying
    // the runtime & config directories, logback.xml, etc.
    AppPathResolver resolver = new AppPathResolver("classroom.");
    Path runtimeDir = resolver.resolveRuntimePath();
    Path configDir = resolver.resolveConfigDir(runtimeDir);

    // Re-init logback if we can find the logback.xml
    Path logbackFile = AppUtils.initLogback(configDir, "classroom.log.config", "logback.xml");

    // Locate the spring file for this app.
    String springConfigPath = resolver.resolveSpringPath(configDir, "classpath:/nc-classroom-grizzly/spring-config.xml");
    String[] activeProfiles = resolver.resolveSpringProfiles(); // defaults to "hosted"

    boolean shuttingDown = Arrays.asList(args).contains("-shutdown");
    String action = (shuttingDown ? "Shutting down" : "Starting");

    log.info("{} server:\n" +
      "  *  Runtime Dir     (classroom.runtime.dir)     {}\n" +
      "  *  Config Dir      (classroom.config.dir)      {}\n" +
      "  *  Logback File    (classroom.log.config)      {}\n" +
      "  *  Spring Path     (classroom.spring.config)   {}\n" +
      "  *  Active Profiles (classroom.active.profiles) {}", action, runtimeDir, configDir, logbackFile, springConfigPath, asList(activeProfiles));

    AbstractXmlApplicationContext applicationContext = SpringUtils.createXmlConfigApplicationContext(springConfigPath, activeProfiles);

    GrizzlyServer grizzlyServer = applicationContext.getBean(GrizzlyServer.class);

    if (shuttingDown) {
      ShutdownUtils.shutdownRemote(grizzlyServer.getConfig());
      log.warn("Shut down server at {}:{}", grizzlyServer.getConfig().getHostName(), grizzlyServer.getConfig().getShutdownPort());
      System.exit(0);
      return;
    }

    // Lastly, start the server.
    grizzlyServer.start();
  }
}
