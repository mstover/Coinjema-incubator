package strategiclibrary.service.webaction.tags;

import javax.servlet.jsp.JspException;

import strategiclibrary.service.webaction.error.Redirector;

public class RedirectionTag extends AbstractWebActionTag {
	private static final long serialVersionUID = 1;
	String script;
	
	public void setScript(String script) {
		this.script = script;
	}
	
	/**
     * Adds an exception mapping to the request parameters.
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException
    {
        if(script == null && bodyContent != null)
        {
            script = bodyContent.getString();
        }
        getContext().getRequest().setAttribute(Redirector.REDIRECT_ON,script);

      return EVAL_PAGE;
    }
	
	
}
