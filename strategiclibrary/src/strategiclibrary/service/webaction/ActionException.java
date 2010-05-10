/*
 * Created on Sep 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction;

import java.io.Serializable;

/**
 * @author <a href="mailto:mstover@glbx.net">Michael Stover </a>
 * @version $Revision: 1.2 $
 */
public class ActionException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1;
   Throwable ultimateCause;
   String localizedMessage;

   boolean fatal = false;;

   /**
    *  
    */
   public ActionException() {
      super();
      // TODO Auto-generated constructor stub
   }

   /**
    * @param message
    */
   public ActionException(String message) {
      super(message);
      setLocalizedMessage(message);
      // TODO Auto-generated constructor stub
   }
   
   public ActionException(String message,boolean isFatal)
   {
      this(message);
      setFatal(isFatal);
   }
   
   public ActionException(String message,Throwable cause,boolean isFatal)
   {
      this(message,cause);
      setFatal(isFatal);
   }

   /**
    * @param message
    * @param cause
    */
   public ActionException(String message, Throwable cause) {
      super(message);
      if(cause instanceof ActionException)
      {
         ultimateCause = ((ActionException) cause).ultimateCause;
      }
      else
      {
         ultimateCause = cause;
      }
   }

   /**
    * @param cause
    */
   public ActionException(Throwable cause) {
      this(cause.getMessage(), cause);
   }

   /**
    * @return boolean
    */
   public boolean isFatal()
   {
      return fatal;
   }

   /**
    * @param b
    *                  void
    */
   public void setFatal(boolean b)
   {
      fatal = b;
   }

   /**
    * If the regular message is unsatisfying for some reason, an alternate
    * message string can be requested that will return the classname of the
    * root exception.
    * 
    * @return
    */
   public String getLocalizedMessage()
   {
      if(localizedMessage != null)
      {
         return localizedMessage;
      }
      if(ultimateCause != null)
      {
         return shorten(ultimateCause.getClass().getName());
      }
      else
      {
         return shorten(this.getClass().getName());
      }
   }

   protected String shorten(String classname)
   {
      int index = classname.lastIndexOf(".") + 1;
      if(index < classname.length())
      {
         return classname.substring(index);
      }
      else
      {
         return classname;
      }
   }

   /**
    * Can only set once
    * @param displayMessage
    *                  The displayMessage to set.
    */
   public void setLocalizedMessage(String localizedMessage)
   {
      if(this.localizedMessage == null)
      {
         this.localizedMessage = localizedMessage;
      }
   }
}
