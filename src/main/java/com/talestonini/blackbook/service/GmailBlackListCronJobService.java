package com.talestonini.blackbook.service;

import com.google.api.services.gmail.Gmail;
import com.talestonini.blackbook.Utils;
import com.talestonini.blackbook.model.GmailAccount;

import javax.mail.MessagingException;
import java.io.IOException;

public class GmailBlackListCronJobService extends GmailBlackListService {

    private static GmailBlackListCronJobService instance;

    public static GmailBlackListCronJobService getInstance() {
        if (instance == null) {
            instance = new GmailBlackListCronJobService();
        }
        return instance;
    }

    private GmailBlackListCronJobService() {
    }

    public void processBlackLists()
            throws IOException, MessagingException {

        for (GmailAccount gmailAccount : gmailAccountRepository.findAll()) {
            Gmail gmailService = Utils.getGmailService(gmailAccount.getUserId());
            processBlackList(gmailService, gmailAccount.getEmailAddress());
        }
    }
}
