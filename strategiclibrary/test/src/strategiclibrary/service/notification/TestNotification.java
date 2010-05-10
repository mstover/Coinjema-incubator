/*
 * Created on Apr 30, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.notification;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.service.notification.DefaultNotificationService;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestNotification extends AbstractTestCase
{
   
   /**
    * @param arg0
    */
   public TestNotification(String arg0) {
      super(arg0);
   }
   
   public void testSendEmail() throws Exception
   {
      new DefaultNotificationService().sendMessage(new String[]{"mikes@lazerinc.com"}, 
            new String[0], new String[0], "strategiclibrary-test", "Test email");
   }
}
