package strategiclibrary.service.webaction.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple class to hold errors that occurred during backend processing.  The ErrorsBean stores only the message from the
 * error.
 * @author <a href="mailto:mstover@glbx.net">Michael Stover</a>
 * @version $Revision: 1.1 $
 */
public class ErrorsBean implements Serializable {
	private static final long serialVersionUID = 1;
    public static final String NAME = "errorBean";

    private List errors;
    private boolean redirect;
    private String redirectPage;
    private Collection errorMessages;

/**
     * @return Returns the errorMessages.
     */
    public Collection getErrorMessages() {
        return errorMessages;
    }

    /**
     * @param errorMessages The errorMessages to set.
     */
    public void setErrorMessages(Collection errorMessages) {
        this.errorMessages = errorMessages;
    }
    
    public void addErrorMessage(String msg)
    {
    	if(msg != null && msg.length() > 0)
    		errorMessages.add(msg);
    }
/**
 * Create a new ErrorsBean.
 *
 */
  public ErrorsBean() {
	 errors = new LinkedList();
     errorMessages = new HashSet();
  }

/**
 * Add an ActionException to the errors bean.  The message must be accurate.
 * @param e
 * void
 */
  public void addError(Exception e)
  {
	 errors.add(e);
  }
  
  public boolean containsError(String errorName)
  {
	  for(Exception error : (List<Exception>)errors)
	  {
		  if(errorName.equals(error.getMessage())) return true;
	  }
	  return false;
  }
	
    /**
     * Get the list of errors that occurred during backend processing.
     * @return
     * List
     */
	public List getErrors()
	{
		return errors;
	}
   
   /**
    * Clear the errors out of the bean.
    * 
    * void
    */
   public void clear()
   {
       errors.clear(); 
       errorMessages.clear();
   }
   
    /**
     * @return
     * boolean
     */
    public boolean isRedirect()
    {
        return redirect;
    }

    /**
     * @param b
     * void
     */
    public void setRedirect(boolean b)
    {
        redirect = b;
    }

   /**
    * @return Returns the redirectPage.
    */
   public String getRedirectPage()
   {
      return redirectPage;
   }
   /**
    * @param redirectPage The redirectPage to set.
    */
   public void setRedirectPage(String redirectPage)
   {
      this.redirectPage = redirectPage;
   }
}