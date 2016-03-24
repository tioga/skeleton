package org.tiogasolutions.skeleton.engine.mock;

import org.springframework.stereotype.Component;
import org.tiogasolutions.app.standard.jaxrs.filters.StandardRequestPrincipalResolver;
import org.tiogasolutions.app.standard.session.Session;

import java.security.Principal;

@Component
public class SkeletonRequestPrincipalResolver implements StandardRequestPrincipalResolver {

    @Override
    public Principal getPrincipal(Session session) {
        final Account account = (Account)session.get("account");
        if (account == null) return null;

        return new Principal() {
            @Override
            public String getName() {
                return account.getEmail();
            }
        };
    }
}
