package com.talestonini.blackbook.repository;

import com.googlecode.objectify.Key;
import com.talestonini.blackbook.model.GmailAccount;

import java.util.List;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GmailAccountRepository {

    private static GmailAccountRepository instance;

    public static GmailAccountRepository getInstance() {
        if (instance == null) {
            instance = new GmailAccountRepository();
        }
        return instance;
    }

    private GmailAccountRepository() {
    }

    public List<GmailAccount> findAll() {
        return ofy().load()
                .type(GmailAccount.class)
                .list();
    }

    public GmailAccount findByEmailAddress(String emailAddress) {
        return ofy().load()
                .key(Key.create(GmailAccount.class, emailAddress))
                .now();
    }

    public void insertAccount(String emailAddress, String userId) {
        GmailAccount gmailAccount = new GmailAccount(emailAddress, userId);
        ofy().save()
                .entity(gmailAccount)
                .now();
    }

    public void deleteAccount(String emailAddress) {
        ofy().delete()
                .type(GmailAccount.class)
                .id(emailAddress)
                .now();
    }
}
