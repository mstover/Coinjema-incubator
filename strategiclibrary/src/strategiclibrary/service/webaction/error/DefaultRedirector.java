/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.error;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.beans.ErrorsBean;
import strategiclibrary.util.Converter;

/**
 * An implementation for the Redirector.
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type="redirectorService")
public class DefaultRedirector implements Redirector
{
	private Map<String,String> errorMappings;
    private Map<String,String> msgMappings;

    /* (non-Javadoc)
     * @see strategiclibrary.service.webaction.error.ErrorService#getRedirectPage(strategiclibrary.service.webaction.HandlerData)
     * Additionally, a page redirect value of 'ignore=true' will cause the redirector to ignore the error, even if a default redirection 
     * page is specified for the error.
     */
    public String getRedirectPage(HandlerData requestInfo)
    {
        ErrorsBean errors = (ErrorsBean) requestInfo.getBean(ErrorsBean.NAME);
        if (errors == null || errors.isRedirect())
        {
            if (errors != null)
            {
                errors.setRedirect(false);
            }
            return null;
        }
        setDisplayMessages(errors,requestInfo);
        List errorList = errors.getErrors();
        Iterator iter = errorList.iterator();
        getLog().debug("Starting iteration on list: " + errorList);
        while (iter.hasNext())
        {
            Exception item = (Exception) iter.next();
            String url = getTransform(requestInfo,item);
            getLog().debug("1. error message = " + item.getMessage() + " redirect to " + url);
            if (url != null && !url.equals("ignore=true"))
            {
                getLog().debug("returning redirect = " + url.toString());
                return url;
            }
        }
        getLog().debug("Starting next iteration on list: " + errorList);
        iter = errorList.iterator();
        while (iter.hasNext())
        {
            Exception item = (Exception) iter.next();
            String url = getTransform(errorMappings,item);
            getLog().debug("2. error message = " + item.getMessage() + " redirect to " + url);
            getLog().debug("Exception configuration =  " + errorMappings);
            if (url != null && !"ignore=true".equals(requestInfo.getParameter(item.getMessage())))
            {
                return url;
            }
        }
        return null;
    }
    
    public String getLogicalRedirect(HandlerData requestInfo) {
    	for(String name : requestInfo.getParamNames(REDIRECT_ON))
    	{
    		String redirect = eval(requestInfo.getParameter(name,"null"),requestInfo);
    		if(redirect != null) return redirect;
    	}
		return null;
	}
    
    private String eval(String value,HandlerData info)
    {
    	
    		Map context = info.getBeanMap();
    		return Converter.getString(groovy.executeString(value, context));
    }

	protected void setDisplayMessages(ErrorsBean errors,HandlerData requestInfo)
    {
       Iterator iter = errors.getErrors().iterator();
       while(iter.hasNext())
      {
         Exception ex = (Exception) iter.next();
            String transform = getTransform(requestInfo,ex,"_msg");
            if(transform != null)
            {
               errors.addErrorMessage(transform);
            }
            else
            {
                errors.addErrorMessage(getTransform(msgMappings,ex));
            }
      }
    }
    
    protected String getTransform(HandlerData requestInfo,Exception error)
    {
       return getTransform(requestInfo,error,"");
    }
    
    protected String getTransform(HandlerData requestInfo,Exception error,String postfix)
    {
    	if(getLog().isDebugEnabled())
    	{
    		getLog().debug("Looking for  " + error.getMessage()+postfix+" in " + requestInfo.getBeanMap());
    	}
       if(requestInfo.hasParam(error.getMessage()+postfix))
       {
          return requestInfo.getParameter(error.getMessage()+postfix);
       }
       else if(requestInfo.hasParam(error.getLocalizedMessage()+postfix))
       {
          return requestInfo.getParameter(error.getLocalizedMessage()+postfix);
       }
       else if(requestInfo.hasParam(error.getClass().getSimpleName()+postfix))
       {
          return requestInfo.getParameter(error.getClass().getSimpleName()+postfix);
       }
       else
       {
          return null;
       }
    }
    
    protected String getTransform(Map transformCodes,Exception error)
    {
        getLog().debug("Transforming error with defaults: " + error.getMessage());
       if(transformCodes.containsKey(error.getMessage()))
       {
          return (String)transformCodes.get(error.getMessage());
       }
       else if(transformCodes.containsKey(error.getLocalizedMessage()))
       {
          return (String)transformCodes.get(error.getLocalizedMessage());
       }
       else if(transformCodes.containsKey(error.getClass().getSimpleName()))
       {
          return (String)transformCodes.get(error.getClass().getSimpleName());
       }
       else
       {
          return (String)transformCodes.get("DEFAULT");
       }
    }
    
    @CoinjemaDependency(method="redirects")
    public void setRedirectMappings(Map<String,String> rm)
    {
        errorMappings = new HashMap<String,String>(rm);
    }
    
    @CoinjemaDependency(method="messages")
    public void setMessageMappings(Map<String,String> mm)
    {
        msgMappings = new HashMap<String,String>(mm);
    }
    
    private TemplateService groovy;
    
    @CoinjemaDependency(type="groovyService",method="templateService")
    public void setTemplateService(TemplateService ts)
    {
    	groovy = ts;
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
