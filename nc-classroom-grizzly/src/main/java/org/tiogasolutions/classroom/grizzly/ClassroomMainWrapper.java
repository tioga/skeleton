package org.tiogasolutions.classroom.grizzly;

import java.net.URL;

public class ClassroomMainWrapper {

  public static void main(String...args) throws Throwable {
    URL location = ClassroomMain.class.getProtectionDomain().getCodeSource().getLocation();
    System.out.println("Starting application from " + location);

    if (location.getPath().endsWith(".jar")) {
      JarClassLoader jcl = new JarClassLoader();
      jcl.invokeStart(ClassroomMain.class.getName(), args);

    } else {
      ClassroomMain.main(args);
    }
  }
}
