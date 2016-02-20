package com.talestonini.blackbook.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * The @Entity tells Objectify about our entity.  We also register it in ObjectifyHelper.java -- very important.
 * <p>
 * This is never actually created, but gives a hint to Objectify about our Ancestor key.
 */
@Entity
public class GmailAccount {

    @Id
    private String emailAddress;

    private String userId;

    public GmailAccount() {
    }

    public GmailAccount(String emailAddress, String userId) {
        assert emailAddress != null && !emailAddress.isEmpty();
        assert userId != null && !userId.isEmpty();

        this.emailAddress = emailAddress;
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
