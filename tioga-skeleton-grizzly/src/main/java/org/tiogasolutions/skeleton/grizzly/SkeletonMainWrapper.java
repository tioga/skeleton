package org.tiogasolutions.skeleton.grizzly;

import org.tiogasolutions.app.standard.system.JarClassLoader;

import java.net.URL;

public class SkeletonMainWrapper {

  public static void main(String...args) throws Throwable {
    URL location = SkeletonMain.class.getProtectionDomain().getCodeSource().getLocation();
    System.out.println("Starting application from " + location);

    if (location.getPath().endsWith(".jar")) {
      JarClassLoader jcl = new JarClassLoader();
      jcl.invokeStart(SkeletonMain.class.getName(), args);

    } else {
      SkeletonMain.main(args);
    }
  }
}
