/*
 * Created on Sep 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject
public class AbstractWebActionTag extends BodyTagSupport
{
    private static final long serialVersionUID = 1;
    private PageContext context;
    private Logger log;
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#setPageContext(javax.servlet.jsp.PageContext)
     */
    public void setPageContext(PageContext context)
    {
        super.setPageContext(context);
        this.context = context;
    }
    
    /**
     * Get the appropriate logger for the current page.
     * @return
     * Logger
     */
    protected Logger getLogger()
    {
        return log;      
    }
    
    @CoinjemaDependency(alias="log4j")
    public void setLogger(Logger l)
    {
        log = l;
    }

    /**
     * Get a reference to the current PageContext
     * @return
     * PageContext
     */
    protected PageContext getContext()
    {
        return context;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release()
    {
        log = null;
        context = null;
        super.release();
    }

}
