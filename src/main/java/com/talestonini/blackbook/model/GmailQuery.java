package com.talestonini.blackbook.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.talestonini.blackbook.controller.ObjectifyHelper;

/**
 * The @Entity tells Objectify about our entity.  We also register it in {@link ObjectifyHelper}.  Our primary key @Id
 * is set automatically by the Google Datastore for us.
 * <p>
 * We add a @Parent to tell the object about its ancestor.  We are doing this to support many accounts.  Objectify,
 * unlike the AppEngine library, requires that you specify the fields you want to index using @Index.  Only indexing the
 * fields you need can lead to substantial gains in performance -- though if not indexing your data from the start will
 * require indexing it later.
 * <p>
 * NOTE - all the properties are PUBLIC so that can keep the code simple.
 **/
@Entity
public class GmailQuery {

    @Parent
    private Key<GmailAccount> gmailAccount;

    @Id
    private Long id;

    @Index
    private String query;

    public GmailQuery() {
    }

    public GmailQuery(String emailAddress, String query) {
        assert emailAddress != null && !emailAddress.isEmpty();

        this.gmailAccount = Key.create(GmailAccount.class, emailAddress); // creating the ancestor key
        this.query = query;
    }

    public Key<GmailAccount> getGmailAccount() {
        return gmailAccount;
    }

    public void setGmailAccount(Key<GmailAccount> gmailAccount) {
        this.gmailAccount = gmailAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
