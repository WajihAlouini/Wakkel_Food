package com.example.test_javafx2;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class testmailing {

    public static void SendMail(String gmail, String sujet, String titre) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.user", "guesmiaymen181@gmail.com");
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.debug", true);
        props.put("mail.smtp.socketFactory.port", 587);
        props.put("mail.smtp.socketFactory.fallback", false);

        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            message.setSubject(sujet); // Titre du message
            message.setFrom(new InternetAddress("guesmiaymen181@gmail.com"));
            message.setText(titre); // Contenu du message
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(gmail));
            try {
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com", "guesmiaymen181@gmail.com", "rbga pqoz ozid lvxg");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                System.out.println("E-mail envoyé avec succès !");
            } catch (Exception e) {
                System.out.println("Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
