package com.ctrlassistant.server;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.security.Security;
import java.util.Properties;

public class GoogleMail {
    /**
     * Send email using GMail SMTP server.
     *
     * @param username GMail username
     * @param password GMail password
     * @param recipientEmail TO recipient
     * @param title title of the message
     * @param body message to be sent
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
     */

    public static void Send(final String username, final String password, String recipientEmail, String title, String body, String file) throws AddressException, MessagingException {
        GoogleMail.Send(username, password, recipientEmail, "", title, body, file);
    }

    public static void Send(final String username, final String password, String recipientEmail, String ccEmail, String title, String body, String file) throws AddressException, MessagingException {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtps.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.setProperty("mail.smtps.auth", "true");
        props.put("mail.smtps.quitwait", "false");

        Session session = Session.getInstance(props, null);

        // -- Create a new message --
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username + "@gmail.com"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail, false));
        if (ccEmail.length() > 0) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail, false));
        }
        message.setSubject(title);

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(body);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        messageBodyPart = new MimeBodyPart();
        DataSource dataSource = new FileDataSource(file);
        messageBodyPart.setDataHandler(new DataHandler(dataSource));
        messageBodyPart.setFileName(title+".csv");

        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        Transport t = session.getTransport("smtps");

        t.connect("smtp.gmail.com", username, password);
        t.sendMessage(message, message.getAllRecipients());
        t.close();
    }
}
