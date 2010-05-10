/*
 * Created on Sep 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * Provides an adapter for web actions that handles accepting the logging, parameters and the service manager.  These items are
 * stored in protected variables.
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject
public abstract class AbstractWebAction implements WebRequestHandler
{
    WebRequestHandlerContainer container;
    
    /**
     * A parameters object with all the config property values of the container system.
     */
    protected Properties props;
    
    public String getConfigProperty(String key)
    {
        return props.getProperty(key,"");
    }

   /**
    * @return Returns the props.
    */
   protected Properties getProps()
   {
      return props;
   }
   
   public void setProps(Properties p)
   {
       props = p;
   }
   
   protected WebRequestHandlerContainer getContainer()
   {
      return container;
   }
   
   void setContainer(WebRequestHandlerContainer c)
   {
       this.container = c;
   }
	
	protected void addMessage(String message, Object obj, HandlerData actionInfo) throws ActionException
   {
      Collection messages = (Collection) actionInfo.getUserBean("messages", LinkedList.class);
      messages.add(new ActionMessage(message,obj));
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
