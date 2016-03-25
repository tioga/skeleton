package org.tiogasolutions.skeleton.engine.mock;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AccountStore {

    private final Map<String,Account> map = new HashMap<>();

    public AccountStore() {
        add(new Account("mickey.mouse@disney.com", "Mickey", "Mouse", "secret"));
        add(new Account("minnie.mouse@disney.com", "Minnie", "Mouse", "secret"));
        add(new Account("donald.duck@disney.com", "Donald", "Duck", "secret"));
        add(new Account("daisy.duck@disney.com", "Daisy", "Duck", "secret"));
    }

    private void add(Account account) {
        map.put(account.getEmail(), account);
    }

    public Account findByEmail(String email) {
        return map.get(email);
    }

    public Collection<Account> getAll() {
        return map.values();
    }
}
