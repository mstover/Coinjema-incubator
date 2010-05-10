/*
 * Created on Sep 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.tags;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import strategiclibrary.service.webaction.HandlerData;



/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class ActionRequest extends AbstractWebActionTag
{
	private static final long serialVersionUID = 1;
    private static String PRE = "zzz";
    
    private String action;
    private String order;
    


    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag() throws JspException
    {
        PageContext context = getContext();
        ServletRequest request = context.getRequest();
        if(order == null)
        {
            order = PRE;
        }
        String key = HandlerData.REQUEST_ACTION + order;
        Object currentValue = request.getAttribute(key);
        if(currentValue == null)
        {
            request.setAttribute(key,action);   
        }
        else if(currentValue instanceof String)
        {
            request.setAttribute(key,new String[]{(String)currentValue,action});
        }
        else if(currentValue instanceof String[])
        {
            String[] newValues = new String[((String[])currentValue).length + 1];
            System.arraycopy((String[])currentValue,0,newValues,0,newValues.length-1);
            newValues[newValues.length-1] = action;
            request.setAttribute(key,newValues);
        }
        return super.doEndTag();
    }

    /**
     * @return
     * String
     */
    public String getAction()
    {
        return action;
    }

    /**
     * @return
     * String
     */
    public String getOrder()
    {
        return order;
    }

    /**
     * @param string
     * void
     */
    public void setAction(String string)
    {
        action = string;
    }

    /**
     * @param string
     * void
     */
    public void setOrder(String string)
    {
        order = PRE + string;
    }

}
