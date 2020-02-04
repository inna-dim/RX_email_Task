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
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.openqa.selenium.remote.server.Session;
import ui.mailbox.EmailPage;
import ui.mailbox.MailboxHomePage;
import ui.mailbox.MailinatorPage;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

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
    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_LABELS);
    private static final String CREDENTIALS_FILE_PATH = "dataSources/credentials.json";

    @Step
    public void opensWebsite(String website) {
        getDriver().navigate().to(website);
    }

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = User.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static MIMEMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MIMEMessage email = new MIMEMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MIMEMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static Message sendMessage(Gmail service,
                                      String userId,
                                      MIMEMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }

    @Step
    public void sendAMessage(String address) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
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
        MIMEMessage mimeMessage = createEmail(address
                , service.users().getProfile("user").getUserIp()
                , subject
                , bodyText);
        Message message = createMessageWithEmail(mimeMessage);
        sendMessage(service,
                service.users().getProfile("user").getUserId(),
                mimeMessage);
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


