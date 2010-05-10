/*
 * Created on Oct 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.notification;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type="emailService")
public class DefaultNotificationService implements
        NotificationService {
    Properties mailProperties;

    Session smtpSession;

    boolean active = true;
    
    public DefaultNotificationService()
    {}
    
    public DefaultNotificationService(CoinjemaContext context)
    {}
    

    @CoinjemaDependency(method = "mailProperties")
    public void setMailProperties(Properties props) {
        this.mailProperties = props;
    }

    @CoinjemaDependency(method = "active", order = CoinjemaDependency.Order.LAST, hasDefault = true)
    public void setActive(boolean a) {
        this.active = a;
        initialize();
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.notification.NotificationService#sendMessage(java.lang.String[],
     *      java.lang.String[], java.lang.String[], java.lang.String,
     *      java.lang.String)
     */
    public void sendMessage(String[] addressees, String[] ccAddressess,
            String[] bccAddressees, String subject, String body) {
        MimeMessage message = new MimeMessage(smtpSession);
        try {
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            message.setFrom(new InternetAddress(mailProperties
                    .getProperty("mail.from")));
            message.setReplyTo(new InternetAddress[] { new InternetAddress(
                    mailProperties.getProperty("mail.replyTo")) });
            sendTo(addressees, ccAddressess, bccAddressees, message);
        } catch (Exception e) {
            getLog().error("Failed to send message", e);
            throw new RuntimeException("Failed to send message", e);
        }
    }

    /**
     * @param addressees
     * @param ccAddressess
     * @param bccAddressees
     * @param message
     * @throws MessagingException
     * @throws AddressException
     * @throws IOException
     */
    protected void sendTo(String[] addressees, String[] ccAddressess,
            String[] bccAddressees, MimeMessage message)
            throws MessagingException, AddressException, IOException {
        message.setRecipients(Message.RecipientType.TO,
                getAddresses(addressees));
        setBccAddresses(message, bccAddressees);
        if (ccAddressess != null) {
            message.setRecipients(Message.RecipientType.CC,
                    getAddresses(ccAddressess));
        }
        if (!active) {
            logMessage(message);
        } else {
            Transport.send(message);
        }
    }

    private void logMessage(MimeMessage message) throws MessagingException,
            IOException {
        getLog().info(
                "Not sending message: " + message.getSubject()
                        + " because inactive:\n\t" + "to: "
                        + arrayToString(message.getAllRecipients()) + "\n\t"
                        + "reply-to: " + arrayToString(message.getReplyTo()) + "\n\t"
                        + "body: " + message.getContent());
    }

    private String arrayToString(Address[] arr) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            buff.append(arr[i].toString());
            if ((+1) < arr.length) {
                buff.append(", ");
            }
        }
        return buff.toString();
    }

    private void setBccAddresses(MimeMessage message, String[] bccAddressees)
            throws MessagingException {
        if (bccAddressees == null) {
            bccAddressees = new String[] { mailProperties
                    .getProperty("bcc.default") };
        } else {
            String[] newArray = new String[bccAddressees.length + 1];
            System.arraycopy(bccAddressees, 0, newArray, 0,
                    bccAddressees.length);
            newArray[newArray.length - 1] = mailProperties
                    .getProperty("bcc.default");
            bccAddressees = newArray;
        }
        message.setRecipients(Message.RecipientType.BCC,
                getAddresses(bccAddressees));
    }

    private Address[] getAddresses(String[] addresses) throws AddressException {
        Collection addrs = new HashSet();
        for (int i = 0; i < addresses.length; i++) {
            if (addresses[i] != null) {
                try {
                    addrs.add(new InternetAddress(addresses[i]));
                } catch (AddressException e) {
                }
            }
        }
        return (Address[]) addrs.toArray(new Address[0]);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() {
            smtpSession = Session.getInstance(mailProperties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.notification.NotificationService#sendMessage(java.lang.String[],
     *      java.lang.String, java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void sendMessage(String[] to, String from, String subject,
            String contentType, String body) {
        MimeMessage message = new MimeMessage(smtpSession);
        try {
            message.setSubject(subject);
            if (contentType == null) {
                contentType = "text/plain";
            }
            message.setContent(body, contentType);
            String replyTo = null;
            if (from == null) {
                from = mailProperties.getProperty("mail.from");
                replyTo = mailProperties.getProperty("mail.replyTo");
            } else {
                replyTo = from;
            }
            message.setFrom(new InternetAddress(from));
            message.setReplyTo(new InternetAddress[] { new InternetAddress(
                    replyTo) });
            sendTo(to, null, null, message);
        } catch (Exception e) {
            getLog().error("Failed to send message", e);
            throw new RuntimeException("Failed to send message", e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.notification.NotificationService#sendMessage(java.lang.String[],
     *      java.lang.String, java.lang.String, java.lang.String)
     */
    public void sendMessage(String[] to, String subject, String contentType,
            String message) {
        sendMessage(to, null, subject, contentType, message);

    }
    
    private Logger log;

    @CoinjemaDependency(alias="log4j")
    public void setLog(Logger l)
    {
        log = l;
    }
    
    protected Logger getLog()
    {
        return log;
    }
}