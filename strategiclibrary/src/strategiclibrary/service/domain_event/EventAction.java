/*
 * Created on Feb 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.domain_event;


/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface EventAction
{
   public  void executeAction(DomainEvent event) throws Exception;
   
}
