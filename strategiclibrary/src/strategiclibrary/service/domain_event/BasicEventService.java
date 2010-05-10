/*
 * Created on Feb 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.domain_event;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * The Domain Event Service provides a framework for scripting reactions to domain events. 
 * Domain events refers to events that are specific to your app's problem domain. 
 * In PMTool, for example, a domain event occurs when a Todo's owner changes, or its state is updated.
 * Essentially, your app notifies the service of events that occur, and the event service executes 
 * (asynchronously) backing scripts that you've defined for specific events. Scripts are XML files that 
 * define behaviors to occur for given events, using EventAction? implementations. 
 * The Giblex library comes with one implemented EventAction? - an Email Action that uses Velocity 
 * templates to create emails to send out. Another action planned is one that will execute arbitrary 
 * SQL statements to allow developers to log events to a database, or to behave as triggers for various events.
 * 
 * Sample config:
 * <pre>&lt;service role="strategiclibrary.service.domain_event.DomainEventService" class="strategiclibrary.service.domain_event.BasicEventService">
      &lt;eventDefinitions dir="@webapp_path@/WEB-INF/eventDefs"/>
      &lt;vmFile/>
      &lt;component name="pmService">strategiclibrary.taskmaster.service.PMToolBusinessLogicService&lt;/component>
      &lt;component name="userGroupService">strategiclibrary.taskmaster.service.UserGroupService&lt;/component>
      &lt;eventRetrieval/>
   &lt;/service></pre> 
 * @author mstover
 *
 */
@CoinjemaObject(type="eventService")
public class BasicEventService implements DomainEventService,Runnable
{
   Map actionMappings;
   boolean running = false;
   LinkedList events = new LinkedList();
   Object sync = new Object();
   
   public static final String EVENT_DEFS = "eventDefinitions";
   public static final String DIR = "dir";
   public static final String VM_FILE = "vmFile";
   public static final String COMPONENT = "components";
   public static final String CONTEXT_NAME = "contextName";
   public static final String EVENT_RETRIEVAL = "eventRetrieval";
   public static final String NAME = "name";
   

   /* (non-Javadoc)
    * @see strategiclibrary.service.domain_event.DomainEventService#eventOccurred(strategiclibrary.service.domain_event.DomainEvent)
    */
   public void eventOccurred(DomainEvent event)
   {
      getLog().debug("Event occurred: " + event.getType());
      events.addLast(event);
      synchronized(sync)
      {
         sync.notify();
      }
   }
   
   protected void doEvent(DomainEvent event)
   {
      List actions = (List)actionMappings.get(event.getType());
      if(actions == null || actions.size() == 0)
      {
         return;
      }
      Iterator iter = actions.iterator();
      while(iter.hasNext())
      {
         EventAction action = (EventAction) iter.next();
         getLog().debug("Executing event: " + event.getType());
         try
         {
            action.executeAction(event);         
         }catch(Exception e)
         {
            getLog().error("Problem with event action: " + event.getType(),e);
         }
      }
   }
   
   public void run()
   {
      while(running)
      {
         while(events.size() > 0)
         {
            doEvent((DomainEvent)events.removeFirst());
         }
         synchronized(sync)
         {
            try
            {
               sync.wait();
            }
            catch(InterruptedException e)
            {
               getLog().warn("Thread interrupted unexpectedly",e);
            }
         }
      }
   }
   
   @CoinjemaDependency(method="actions",order=CoinjemaDependency.Order.LAST)
   public void setActions(Map actions)
   {
       this.actionMappings = actions;
       start();
   }
   
   /**
    * Start the event service.  The event service is asynchronous and starts a thread to process events.
    */
   public void start()
   {
      running = true;
      new Thread(this).start();
      getLog().debug("Domain Event service started");

   }
   /**
    * Stops the event service thread.
    */
   public void stop()
   {
      running = false;
      synchronized(sync)
      {
         sync.notify();
      }
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
