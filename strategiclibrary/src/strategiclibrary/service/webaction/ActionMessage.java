/*
 * Created on Sep 20, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.webaction;

import java.io.Serializable;


/**
 * @author mstover
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ActionMessage implements Serializable
{
	private static final long serialVersionUID = 1;
   String msg;
   Object ref;
   
   public ActionMessage(String msg,Object ref)
   {
      this.msg = msg;
      this.ref = ref;
   }
   
   public ActionMessage()
   {}
   
   public String toString()
   {
      return msg;
   }
   /**
    * @return Returns the msg.
    */
   public String getMsg()
   {
      return msg;
   }
   /**
    * @param msg The msg to set.
    */
   public void setMsg(String msg)
   {
      this.msg = msg;
   }
   /**
    * @return Returns the ref.
    */
   public Object getRef()
   {
      return ref;
   }
   /**
    * @param ref The ref to set.
    */
   public void setRef(Object ref)
   {
      this.ref = ref;
   }
}
