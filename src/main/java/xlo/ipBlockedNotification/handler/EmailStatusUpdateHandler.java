package xlo.ipBlockedNotification.handler;

import sun.net.smtp.SmtpClient;
import xlo.ipBlockedNotification.model.IpInfo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class EmailStatusUpdateHandler implements StatusUpdateHandler {

    private String host;
    private String from;
    private String to;
    private String password;

    public EmailStatusUpdateHandler(String host, String from, String to, String password) {
        this.host = host;
        this.from = from;
        this.to = to;
        this.password = password;
    }

    @Override
    public void handleStatusUpdate(IpInfo ipInfo) throws MessagingException {
        Properties properties = System.getProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.host", this.host);
        properties.put("mail.smtp.auth", false);
        Session session = Session.getDefaultInstance(properties);
        Message message = new MimeMessage(session);
        InternetAddress fromAddress = new InternetAddress(this.from);
        InternetAddress[] toAddress = InternetAddress.parse(this.to, false);

        message.setFrom(fromAddress);
        message.setRecipients(Message.RecipientType.TO, toAddress);
        message.setSubject(ipInfo.getHost() + ":" + ipInfo.getPort() + " status update");
        message.setText("ping: " + ipInfo.getPingStatus().getStatus().toString() + "; connect: " + ipInfo.getConnectStatus().getStatus().toString());
        message.setSentDate(new Date());

        Transport transport = session.getTransport();
        transport.connect(this.host, this.from, this.password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
