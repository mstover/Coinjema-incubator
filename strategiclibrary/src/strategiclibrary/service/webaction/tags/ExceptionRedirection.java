/*
 * Created on Sep 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.tags;

import javax.servlet.jsp.JspException;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class ExceptionRedirection extends AbstractWebActionTag
{
	private static final long serialVersionUID = 1;
    private String error;
    private String url;
    private String ignore;
    private String message;

    /**
     * Adds an exception mapping to the request parameters.
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException
    {
        if(url == null && bodyContent != null)
        {
            url = bodyContent.getString();
        }
        this.getLogger().debug("setting redirect for error: " + error + " to url + " + url + " from tag: " + this);
    	if(ignore == null && url != null)
    	{	
    		getContext().getRequest().setAttribute(error,url);
    	}
    	else if(ignore != null)
    	{
    		getContext().getRequest().setAttribute(error,"ignore=true");
    	}
      if(message != null)
      {
         getContext().getRequest().setAttribute(error+"_msg", message);
      }
      url = null;

      return EVAL_PAGE;
    }

    /**
     * @return
     * String
     */
    public String getError()
    {
        return error;
    }

    /**
     * @return
     * String
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param string
     * void
     */
    public void setError(String string)
    {
        error = string;
    }

    /**
     * @param string
     * void
     */
    public void setUrl(String string)
    {
        url = string;
    }

	/**
	 * @return Returns the ignore.
	 */
	public String getIgnore() {
		return ignore;
	}

	/**
	 * @param ignore The ignore to set.
	 */
	public void setIgnore(String ignore) {
		this.ignore = ignore;
	}

   /**
    * @return Returns the message.
    */
   public String getMessage()
   {
      return message;
   }
   /**
    * @param message The message to set.
    */
   public void setMessage(String message)
   {
      this.message = message;
   }
}
