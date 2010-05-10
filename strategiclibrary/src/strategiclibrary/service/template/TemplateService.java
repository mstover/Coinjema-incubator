/*
 * Created on Oct 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.template;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public interface TemplateService
{
    public boolean mergeTemplate(String templateName,Map dataContext,Writer results);
    
    public boolean evaluate(Map context,Writer results,String logTag,Reader template);
    
    public boolean evaluate(Map context,Writer results,String logTag,String in);
    
    /**
     * Executes a dynamic script.  Returns the object returned by the script.
     * @param scriptName
     * @param context
     * @return
     * @throws ServiceException
     */
    public Object execute(String scriptName,Map context);
    
    public Object executeString(String script,Map context);
}
