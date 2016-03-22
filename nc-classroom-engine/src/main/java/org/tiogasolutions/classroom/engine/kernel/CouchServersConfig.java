package org.tiogasolutions.classroom.engine.kernel;

public class CouchServersConfig {

  private final String url;
  private final String databaseName;

  private final String username;
  private final String password;

  public CouchServersConfig(String url, String databaseName, String username, String password) {
    this.url = url;
    this.databaseName = databaseName;
    this.username = username;
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public String getDatabaseName() {
    return databaseName;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}
