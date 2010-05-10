/*
 * Created on Feb 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.domain_event;


/**
 * @author mstover
 *
 * The DomainEventService reads declarative logic (in the form of xml) that defines behavior for events (such as the updating
 * of business domain objects in a database).  Supported actions are email notification sent to interested parties, and
 * information stored in a db.  Also, retrieval of past events given an object that defines criteria for what events to retrieve (this 
 * assumes a backing db that stores the events).
 */
public interface DomainEventService
{
   /**
    * Notify the Event Service that an event occurred.  The event object contains information about the source of
    * the event, the actor who caused it.
    * @param event
    * @throws ServiceException
    */
   public void eventOccurred(DomainEvent event);
}
