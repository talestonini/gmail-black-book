package com.talestonini.blackbook.service;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.talestonini.blackbook.model.GmailAccount;
import com.talestonini.blackbook.model.GmailQuery;
import com.talestonini.blackbook.repository.GmailAccountRepository;
import com.talestonini.blackbook.repository.GmailQueryRepository;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GmailBlackListService {

    private static GmailBlackListService instance;

    public static GmailBlackListService getInstance() {
        if (instance == null) {
            instance = new GmailBlackListService();
        }
        return instance;
    }

    protected GmailAccountRepository gmailAccountRepository;

    protected GmailQueryRepository gmailQueryRepository;

    protected GmailBlackListService() {
        this.gmailAccountRepository = GmailAccountRepository.getInstance();
        this.gmailQueryRepository = GmailQueryRepository.getInstance();
    }

    public void insertQuery(String emailAddress, String userId, String query) {
        GmailAccount gmailAccount = gmailAccountRepository.findByEmailAddress(emailAddress);
        if (gmailAccount == null) {
            gmailAccountRepository.insertAccount(emailAddress, userId);
        }
        gmailQueryRepository.insertQuery(emailAddress, query);
    }

    public void deleteQuery(String emailAddress, long queryId) {
        gmailQueryRepository.deleteQuery(emailAddress, queryId);
        if (gmailQueryRepository.findByEmailAddress(emailAddress).isEmpty()) {
            gmailAccountRepository.deleteAccount(emailAddress);
        }
    }

    public void processBlackList(Gmail gmailService, String emailAddress)
            throws IOException, MessagingException {

        for (GmailQuery gmailQuery : gmailQueryRepository.findByEmailAddress(emailAddress)) {
            deleteMessages(gmailService, emailAddress, gmailQuery.getQuery());
        }
    }

    private void deleteMessages(Gmail gmailService, String emailAddress, String query)
            throws IOException, MessagingException {

        List<Message> messages = findMessages(gmailService, emailAddress, query);

        if (messages.isEmpty()) {
            System.out.println("no messages found");
            return;
        }

        for (Message message : messages) {
            String messageId = message.getId();

            MimeMessage mimeMsg = getMimeMessage(gmailService, emailAddress, messageId);
            String msgSubject = mimeMsg.getSubject();

            gmailService.users().messages()
                    .delete(emailAddress, messageId)
                    .execute();

            System.out.println(String
                    .format("message deleted successfully (id: %s subject: %s)", messageId, msgSubject));
        }
    }

    private List<Message> findMessages(Gmail gmailService, String emailAddress, String query)
            throws IOException {

        ListMessagesResponse response = gmailService.users().messages()
                .list(emailAddress).setQ(query)
                .setIncludeSpamTrash(true)
                .execute();

        List<Message> messages = new ArrayList<>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();

                response = gmailService.users().messages()
                        .list(emailAddress).setQ(query)
                        .setPageToken(pageToken)
                        .setIncludeSpamTrash(true)
                        .execute();
            } else {
                break;
            }
        }
        return messages;
    }

    /**
     * Get a Message and use it to create a MimeMessage.
     *
     * @param emailAddress the user's email address; the special value "me" can be used to indicate the authenticated
     *                     user
     * @param messageId    id of message to retrieve
     * @return a mime message populated from retrieved message
     * @throws IOException
     * @throws MessagingException
     */
    private MimeMessage getMimeMessage(Gmail gmailService, String emailAddress, String messageId)
            throws IOException, MessagingException {

        Message message = gmailService.users().messages()
                .get(emailAddress, messageId)
                .setFormat("raw")
                .execute();

        byte[] emailBytes = Base64.decodeBase64(message.getRaw());

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        return new MimeMessage(session, new ByteArrayInputStream(emailBytes));
    }
}
