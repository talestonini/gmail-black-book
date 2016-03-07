package com.talestonini.blackbook.controller;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.User;
import com.talestonini.blackbook.Utils;
import com.talestonini.blackbook.service.GmailBlackListService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FrontController extends AbstractAppEngineAuthorizationCodeServlet {

    public enum Action {
        INSERT_QUERY, DELETE_QUERY
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // get the action in question
        String action = req.getParameter("action");
        assert action != null && !action.isEmpty();
        Action actionType = Action.valueOf(action);

        User user = Utils.currentUser();
        GmailBlackListService gmailBlackListService = GmailBlackListService.getInstance();

        switch (actionType) {
            case INSERT_QUERY:
                String query = req.getParameter("query");
                if (query != null && !query.isEmpty()) {
                    gmailBlackListService.insertQuery(user.getEmail(), user.getUserId(), query);
                }
                break;
            case DELETE_QUERY:
                String queryId = req.getParameter("queryId");
                if (queryId != null && !queryId.isEmpty()) {
                    gmailBlackListService.deleteQuery(user.getEmail(), Long.valueOf(queryId));
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
