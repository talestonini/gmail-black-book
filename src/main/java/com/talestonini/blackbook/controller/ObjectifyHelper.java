package com.talestonini.blackbook.controller;

import com.googlecode.objectify.ObjectifyService;
import com.talestonini.blackbook.model.GmailAccount;
import com.talestonini.blackbook.model.GmailQuery;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * ObjectifyHelper, a ServletContextListener, is setup in web.xml to run before a JSP is run.  This is required to let
 * JSP's access Objectify.
 */
public class ObjectifyHelper implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // this will be invoked as part of a warm-up request, or the first user request if no warm-up request
        ObjectifyService.register(GmailAccount.class);
        ObjectifyService.register(GmailQuery.class);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method
    }
}
