/*
 * Created on Oct 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.notification;


/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public interface NotificationService
{
    /**
     * Sends an email to the indicated addresses with the given subject and body.
     * @param addressees
     * @param ccAddressess
     * @param bccAddressees
     * @param subject
     * @param body
     * void
     */
    public void sendMessage(String[] addressees,String[] ccAddressess,String[] bccAddressees,String subject,String body);
    
    public void sendMessage(String[] to,String from,String subject,String contentType,String message);
    public void sendMessage(String[] to,String subject,String contentType,String message);
}
