package com.talestonini.blackbook.controller;

import com.talestonini.blackbook.service.GmailBlackListCronJobService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProcessCronJob extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            GmailBlackListCronJobService.getInstance().processBlackLists();
        } catch (MessagingException me) {
            throw new ServletException(me);
        }
    }
}
