package com.talestonini.blackbook;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

import static com.google.api.client.util.Preconditions.checkArgument;

public class Utils {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

    private static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();

    private static final String APPLICATION_NAME = "Gmail Black Book";

    private static GoogleClientSecrets clientSecrets = null;

    private Utils() {
    }

    public static Gmail getGmailService(String userId) throws IOException {
        Credential credential = newFlow().loadCredential(userId);
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static GoogleAuthorizationCodeFlow newFlow() throws IOException {
        return new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, getClientCredential(),
                Collections.singleton(GmailScopes.MAIL_GOOGLE_COM))
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
    }

    public static String getRedirectUri(HttpServletRequest req) {
        GenericUrl url = new GenericUrl(req.getRequestURL().toString());
        url.setRawPath("/oauth2callback");
        return url.build();
    }

    private static GoogleClientSecrets getClientCredential() throws IOException {
        if (clientSecrets == null) {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(Utils.class.getResourceAsStream("/client_secrets.json")));

            checkArgument(!clientSecrets.getDetails().getClientId().startsWith("Enter ")
                            && !clientSecrets.getDetails().getClientSecret().startsWith("Enter "),
                    "Download client_secrets.json file from Google Cloud Platform Console.");
        }
        return clientSecrets;
    }
}
