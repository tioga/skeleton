package org.tiogasolutions.skeleton.engine.mock;

import java.io.Serializable;

public class Account implements Serializable {

    private final String email;
    private final String firstName;
    private final String lastName;
    private final String password;

    public Account(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }
}
