package com.sagatechs.generics.service;

import com.sagatechs.generics.appConfiguration.AppConfigurationKey;
import com.sagatechs.generics.appConfiguration.AppConfigurationService;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@ApplicationScoped
public class EmailService {

    private final static Logger LOGGER = Logger.getLogger(EmailService.class);

    @Inject
    AppConfigurationService appConfigurationService;


    private Session session;


    private String mailUsername ;
    private String mailPassword ;
    private String adminEmailAdress;


    @PostConstruct
    public void init() {
        try {
            Properties prop = new Properties();



            prop.put("mail.smtp.host", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_HOST));
            prop.put("mail.smtp.port", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_PORT));
            prop.put("mail.smtp.auth", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP));
            prop.put("mail.smtp.socketFactory.poºrt", this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_SMTP_PORT));
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailUsername = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_USERNAME);
            mailPassword = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_PASSOWRD);
            adminEmailAdress = this.appConfigurationService.findValorByClave(AppConfigurationKey.EMAIL_ADDRES);

            session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailUsername, mailPassword);
                }
            });
        } catch (Exception e) {
            LOGGER.error("Error en la configuración de la cuenta de correo electrónico del administrador.");
        }
    }

    @Asynchronous
    public void sendEmailMessage(String destinationAdress, String subject, String messageText) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(adminEmailAdress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinationAdress));
            message.setSubject(subject);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            //Set key values
            @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
            Map<String, String> input = new HashMap<>();
            input.put("Author", "java2db.com");
            input.put("Topic", "HTML Template for Email");
            input.put("Content In", "English");

            //mimeBodyPart.setText(readEmailFromHtml("../webapp/resources/templates/email.html", input));
            mimeBodyPart.setText(messageText);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    protected String readEmailFromHtml(String filePath, Map<String, String> input) {
        String msg = readContentFromFile(filePath);
        try {
            Set<Map.Entry<String, String>> entries = input.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return msg;
    }

    //Method to read HTML file as a String
    private String readContentFromFile(String fileName) {
        @SuppressWarnings("StringBufferMayBeStringBuilder")
        StringBuffer contents = new StringBuffer();

        try {
            //use buffering, reading one line at a time
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }
                } finally {
                    reader.close();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return contents.toString();
    }
}



