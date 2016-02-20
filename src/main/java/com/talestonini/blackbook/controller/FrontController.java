package com.talestonini.blackbook.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.talestonini.blackbook.Utils;
import com.talestonini.blackbook.service.GmailBlackListService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends AbstractAppEngineAuthorizationCodeServlet {

    public enum Action {
        INSERT_QUERY, DELETE_QUERY, RUN_NOW
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // get the action in question
        String action = req.getParameter("action");
        assert action != null && !action.isEmpty();
        Action actionType = Action.valueOf(action);

        // get the current user id
        String userId = req.getParameter("userId");
        assert userId != null && !userId.isEmpty();

        // get the current user email address
        String emailAddress = req.getParameter("emailAddress");
        assert emailAddress != null && !emailAddress.isEmpty();

        GmailBlackListService gmailBlackListService = GmailBlackListService.getInstance();

        switch (actionType) {
            case INSERT_QUERY:
                String query = req.getParameter("query");
                if (query != null && !query.isEmpty()) {
                    gmailBlackListService.insertQuery(emailAddress, userId, query);
                }
                break;
            case DELETE_QUERY:
                String queryId = req.getParameter("queryId");
                if (queryId != null && !queryId.isEmpty()) {
                    gmailBlackListService.deleteQuery(emailAddress, Long.valueOf(queryId));
                }
                break;
            case RUN_NOW:
                try {
                    gmailBlackListService.processBlackList(Utils.getGmailService(userId), emailAddress);
                } catch (MessagingException me) {
                    throw new ServletException(me);
                }
                break;
        }

        resp.sendRedirect("/blackbook.jsp");
    }

    @Override
    protected AuthorizationCodeFlow initializeFlow()
            throws ServletException, IOException {

        return Utils.newFlow();
    }

    @Override
    protected String getRedirectUri(HttpServletRequest req)
            throws ServletException, IOException {

        return Utils.getRedirectUri(req);
    }
}
