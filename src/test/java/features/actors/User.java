package features.actors;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import ui.mailbox.EmailPage;
import ui.mailbox.MailboxHomePage;
import ui.mailbox.MailinatorPage;


import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mortbay.jetty.HttpMethods.GET;

import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONObject;

public class User extends ScenarioSteps {
    EmailPage emailPage;
    MailinatorPage mailinatorPage;
    MailboxHomePage mailboxHomePage;

    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
     static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    @Step
    public void opensWebsite(String website) {
        getDriver().navigate().to(website);
    }

    @Step
    public void sendAMessage(String address) throws Exception {
        final String username = "*******@gmail.com";
        final String password = "*******";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            MimeMessage message = new MimeMessage(session);
           message.setFrom(new InternetAddress("from@gmail.com"));
            message.setRecipients(
                    MimeMessage.RecipientType.TO,
                    InternetAddress.parse("testing1234@mailinator.com")
            );
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String subject = "TEST AT " + dtf.format(now).toString();
            String bodyText = "The Waive Cancellation Fee approval is required for the Opportunity Z6e752Company\n" +
                    "\n" +
                    "\n" +
                    "Please click the link below to open the opportunity:\n" +
                    "https://my.salesforce.com/0061q000008DdpF\n" +
                    "\n" +
                    "To approve or reject this Waive Cancellation Fee via e-mail, use the reply function in your email program to send it back for approval or rejection.\n" +
                    "\n" +
                    "\n" +
                    "To approve the Waive Cancellation Fee, reply with the word 'Approve' as the first word of your reply.\n" +
                    "\n" +
                    "To reject the Waive Cancellation Fee, reply with the word 'Reject' as the first word of your reply.\n" +
                    "\n" +
                    "Many thanks";
            message.setSubject(subject);
            message.setText(bodyText);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void fillsInboxField(String email) {
        mailboxHomePage.emailField.sendKeys(email);
        mailboxHomePage.goButton.click();
    }

    public void opensLatestMessage() {
        mailinatorPage.latestMessage.click();
    }

    public void isAbleToGetLinkFromAnEmail() {
        getDriver().switchTo().frame(emailPage.MESSAGE_FRAME);
        String emailBody = emailPage.emailBody.getText();
        String[] splitted = emailBody.split("http://");
        String url = ("http://" + splitted[1]).split(" ")[0];
        assertThat(url.length()).isNotZero();
    }
}


