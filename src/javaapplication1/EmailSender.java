package javaapplication1;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {

    private static String USER_NAME = "senderEmail@gmail.com"; // GMail user name (just the part before
    // "@gmail.com")
    private static String PASSWORD = "senderPassword"; // GMail password
    private List<String> recipents;
    private String subject;
    private String body;
    
    
    public static void main(final String[] args) {
        final String from = EmailSender.USER_NAME;
        final String pass = EmailSender.PASSWORD;

        EmailSender sendEmail = new EmailSender();
        ArrayList<String> recepients = new ArrayList<String>();
        recepients.add("def@gmail.com");
        
        sendEmail.setRecipents(recepients);
        sendEmail.setSubject("Hello Wrold subject");
        sendEmail.setBody("Hello Wrold body");
        
        sendEmail.sendFromGMail();
        
    }

    public String sendFromGMail() {
        final Properties props = System.getProperties();

        final String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        final Session session = Session.getDefaultInstance(props);
        final MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(USER_NAME));
            List<InternetAddress> toAddress = new ArrayList<InternetAddress>();

            // To get the array of addresses
            ListIterator<String> iterator;
            iterator = this.getRecipents().listIterator();

            while(iterator.hasNext()) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(iterator.next()));
            }
            
            message.setSubject(subject);
            message.setText(body);
            // Email Attachment
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("I adding attachement..!!");
            final Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();

//            URL url = this.getClass().getResource("/TestEmailAttachment.txt");
            URL url = getClass().getClassLoader().getResource("images/TestEmailAttachment.txt");
            System.out.println(url);
            
            final DataSource source = new FileDataSource("TestEmailAttachment.txt");
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("TestEmailAttachment.txt");
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);

            final Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Email Sent SuccessFully");
        } catch (final AddressException ae) {
            ae.printStackTrace();
        } catch (final MessagingException me) {
            me.printStackTrace();
        }
        
        return "SUCCESS";
    }

    /**
     * @return the recipents
     */
    public List<String> getRecipents() {
        return recipents;
    }

    /**
     * @param recipents the recipents to set
     */
    public void setRecipents(List<String> recipents) {
        this.recipents = recipents;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    
}
