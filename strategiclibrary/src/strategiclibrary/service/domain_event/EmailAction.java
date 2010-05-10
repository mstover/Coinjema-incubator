/*
 * Created on Mar 2, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.domain_event;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.template.TemplateService;

/**
 * Implements an email action that reads an email configuration template (XML) that describes an email
 * to send given a Domain Event.
 * 
 * @author Michael Stover
 */
@CoinjemaObject(type="emailEvent")
public class EmailAction extends AbstractAction implements EventAction
{
    private TemplateService templateService;
    private NotificationService emailService;
   String subjectTemplate;
   String bodyTemplate;
   List to;
   List cc;
   List bcc;
   
   /* (non-Javadoc)
    * @see strategiclibrary.service.domain_event.EventAction#executeAction(strategiclibrary.service.domain_event.DomainEvent)
    */
   public void executeAction(DomainEvent event) throws Exception
   {
      Map context = getComponents();
      context.put("event", event);
      String[] tos = translateAddresses(to,context);
      String[] ccs = translateAddresses(cc,context);
      String[] bccs = translateAddresses(bcc,context);
      if(tos.length > 0 || ccs.length > 0 || bccs.length > 0)
      {
          emailService.sendMessage(tos,ccs,bccs, 
               translate(subjectTemplate,context), translate(bodyTemplate,context));
      }
   }   
   

   /**
    * @param tService
    * @param context
    * @throws ServiceException
    */
   private String translate(String template,Map context)
   {
      StringWriter writer = new StringWriter();
      getTemplateService().evaluate(context, writer, getActionName(), template);
      String trans = writer.toString();
      getLog().debug("successfully translated: " + template + " TO: " + trans);
      return trans;
   }
   
   private String[] translateAddresses(List addrs,Map context)
   {
      Iterator iter = addrs.iterator();
      Set allAddrs = new HashSet();
      while(iter.hasNext())
      {
         String template = (String) iter.next();
         String result = translate(template,context);
         addTokens(result,allAddrs);         
      }
      getLog().debug("address list = " + allAddrs);
      return (String[])allAddrs.toArray(new String[0]);
   }
   
   private void addTokens(String s,Collection list)
   {
      StringTokenizer tokens = new StringTokenizer(s,",");
      while(tokens.hasMoreTokens())
      {
         String t = tokens.nextToken().trim();
         if(t.length() > 0)
         {
            list.add(t);
         }
      }
   }
   

   /* (non-Javadoc)
    * @see strategiclibrary.service.domain_event.EventAction#getActionName()
    */
   public String getActionName()
   {
      return "email";
   }
   
   public void setSubjectTemplate(String subject)
   {
       subjectTemplate = subject;
   }
   
   public void setBodyTemplate(String body)
   {
       bodyTemplate = body;
   }
   
   public void setToAddresses(List to)
   {
       this.to = to;
   }
   
   public void setBccAddresses(List bcc)
   {
       this.bcc = bcc;
   }
   
   public void setCcAddresses(List cc)
   {
       this.cc = cc;
   }

    /**
     * @return Returns the templateService.
     */
    public TemplateService getTemplateService() {
        return templateService;
    }

    /**
     * @param templateService The templateService to set.
     */
    @CoinjemaDependency(type="emailEventTemplateService")
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }
    
    @CoinjemaDependency(type="emailEventNotificationService")
    public void setNotificationService(NotificationService ns)
    {
        this.emailService = ns;
    }
}
