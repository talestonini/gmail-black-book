Gmail Black Book
================

This app allows you to set **HARD** filters for your Gmail inbox! 

The main (and only) page is **BLACK** and very straightforward. It provides:

- Sign in/out links to your Gmail account
- Greeting with your Gmail nickname ;)
- Input box to insert filters to your Gmail inbox
- The list of filters already in place
- Option to delete each filter individually
- Option to process the black list manually

Have a look at it live at the Google App Engine: [https://black-book-1221.appspot.com](https://black-book-1221.appspot.com)

The idea is simple. If you create a filter in Gmail to delete unwanted emails, they are not really deleted! They simply
sit in the spam or bin folders. Very annoying! I was sick of that! I didn't even want to see some of those emails coming
anymore, or having to manually empty my spam or bin folders :/

In this app you can create filters using the same syntax Gmail allows you to create filters within their settings and
have these filters processed to erase unwanted emails once and for all *automatically*. Behind the scenes a cron job
will run each of your filters every 5 minutes and get rid of those irritating emails even when they are already in the
spam or bin folders.

That's the best way to tell some people to get a life!

Tech
----

If you want to run your own app of this, you'll need to create a project in the Google Cloud Platform. You'll also need
to add a Credential to it (of the web app type) and configure redirection to your Google App Engine assigned domain
concatenated with `/oauth2callback`. Download the Credential client secret Json file and substitute the
`client_secrets.json` in your repository clone under `.../src/main/resources`. Don't forget to enable the Gmail API!

### Developing and Deploying

Tools:

- Java 1.7 or above
- Maven

To run locally (it does not work well with the redirection unless using Credential of type other):

`mvn appengine:devserver`

To deploy into Google App Engine:

`mvn appengine:update`
