package com.talestonini.blackbook.repository;

import com.googlecode.objectify.Key;
import com.talestonini.blackbook.model.GmailAccount;
import com.talestonini.blackbook.model.GmailQuery;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GmailQueryRepository {

    private static GmailQueryRepository instance;

    public static GmailQueryRepository getInstance() {
        if (instance == null) {
            instance = new GmailQueryRepository();
        }
        return instance;
    }

    private GmailQueryRepository() {
    }

    public List<GmailQuery> findByEmailAddress(String emailAddress) {
        // create the correct ancestor key
        Key<GmailAccount> gmailAccount = Key.create(GmailAccount.class, emailAddress);

        // run an ancestor query to view the GmailQuery'ies belonging to the selected GmailAccount
        return ofy().load()
                .type(GmailQuery.class) // we want only GmailQuery'ies
                .ancestor(gmailAccount) // belonging to the GmailAccount
                .order("query")         // query field is indexed
                .list();
    }

    // use Objectify to insert/delete the GmailQuery and now() is used to make the call synchronously
    // as we will immediately get a new page using redirect and we want the data to be present

    public void insertQuery(String emailAddress, String query) {
        GmailQuery gmailQuery = new GmailQuery(emailAddress, query);
        ofy().save().entity(gmailQuery).now();
    }

    public void deleteQuery(String emailAddress, Long queryId) {
        Key<GmailAccount> gmailAccount = Key.create(GmailAccount.class, emailAddress);
        ofy().delete().type(GmailQuery.class).parent(gmailAccount).id(queryId).now();
    }
}
