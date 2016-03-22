package org.tiogasolutions.classroom.engine.writers;

import org.tiogasolutions.classroom.engine.kernel.Session;

import java.util.HashMap;
import java.util.Map;

public class Thymeleaf {

  private final String view;
  private final Map<String, Object>  variables = new HashMap<>();

  public Thymeleaf(Session session, String view, Object model) {
    this.view = view;
    this.variables.put("it", model);
    if (session != null) {
      this.variables.put("emailAddress", session.getEmailAddress());
    }
  }

  public String getView() {
    return view;
  }

  public Map<String, ?> getVariables() {
    return variables;
  }
}
