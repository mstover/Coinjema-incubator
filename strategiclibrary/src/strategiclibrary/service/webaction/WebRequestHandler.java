/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction;



/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public interface WebRequestHandler
{
    public static String ROLE = WebRequestHandler.class.getName();
    
    /**
     * Performs an action using the given HandlerData to work with.  
     * @param actionInfo
     * @throws ServiceException
     * void
     */
    public void performAction(HandlerData actionInfo) throws ActionException;
    
    /**
     * The name of the action handler - used to route action requests to the appropriate action handler.
     * @return
     * String
     */
    public String getName();
    
}
