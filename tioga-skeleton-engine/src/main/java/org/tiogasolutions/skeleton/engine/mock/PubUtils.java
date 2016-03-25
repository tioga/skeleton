package org.tiogasolutions.skeleton.engine.mock;

import org.tiogasolutions.pub.*;
import org.tiogasolutions.skeleton.engine.resources.DetailLevel;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PubUtils {

    private final UriInfo uriInfo;

    public PubUtils(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public PubAccounts convert(DetailLevel detailLevel, Collection<Account> accounts, int index, int pageSize, int total) {

        PubLinks links = new PubLinks(
                new PubLink("self",     getAccountsUri(index, pageSize)),
                new PubLink("first",    uriInfo.getBaseUriBuilder().path("api/accounts").queryParam("pageSize", pageSize).queryParam("index", 0).build()),
                new PubLink("previous", uriInfo.getBaseUriBuilder().path("api/accounts").queryParam("pageSize", pageSize).queryParam("index", Math.max(0,index-pageSize)).build()),
                new PubLink("next",     uriInfo.getBaseUriBuilder().path("api/accounts").queryParam("pageSize", pageSize).queryParam("index", index+pageSize).build()),
                new PubLink("last",     uriInfo.getBaseUriBuilder().path("api/accounts").queryParam("pageSize", pageSize).queryParam("index", total-pageSize).build())
        );

        PubEmbedded embedded = new PubEmbedded();
        List<PubItem<PubAccount>> list = new ArrayList<>();

        for (Account account : accounts) {

            PubLinks accountLinks = toLinks(account);
            PubAccount pubAccount = convert(account);

            if (DetailLevel.FULL == detailLevel) {
                list.add(new PubItem<>(accountLinks, pubAccount));
            } else {
                list.add(new PubItem<>(accountLinks));
            }
        }

        embedded.put("accounts", list);

        return new PubAccounts(links, accounts.size(), total, index, pageSize, embedded);
    }

    public URI getAccountsUri(Object index, Object pageSize) {
        return uriInfo
                .getBaseUriBuilder()
                .path("api/accounts")
                .queryParam("index", index)
                .queryParam("pageSize", pageSize)
                .build();
    }

    public PubLinks toLinks(Account account) {
        PubLinks links = new PubLinks();
        links.add("self", uriInfo.getBaseUriBuilder().path("api/accounts").path(account.getId()).build());
        links.add("accounts", getAccountsUri(0, PubAccounts.DEFAULT_PAGE_SIZE));
        return links;
    }

    public PubAccount convert(Account account) {
        return new PubAccount(
                account.getId(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName()
        );
    }

    public Response ok(PubItem item) {
        PubLinks links = item.get_links();
        return ok(item, links);
    }

    public Response ok(Object object, PubLinks links) {
        Response.ResponseBuilder builder = Response.ok(object);
        for (PubLink link : links.getLinks()) {
            builder.link(link.getHref(), link.getRel());
        }
        return builder.build();
    }
}
