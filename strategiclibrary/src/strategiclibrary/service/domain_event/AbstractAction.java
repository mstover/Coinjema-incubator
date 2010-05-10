/*
 * Created on Sep 16, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.domain_event;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;


/**
 * @author mstover
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
@CoinjemaObject
public abstract class AbstractAction implements EventAction
{
    Map<String,Object> components;
	
   protected Map getComponents()
   {
      Map comps = new HashMap();
      for(String item : components.keySet())
      {
         comps.put(item, components.get(item));         
         getLog().debug("component " + item + " added to context with value: " + comps.get(item));
      }
      return comps;
   }
   
   @CoinjemaDependency(method="componentList")
   public void setComponentList(Map<String,Object> c)
   {
       components = c;
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
