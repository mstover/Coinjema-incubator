/*
 * Created on Jan 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.sql;

import java.util.HashMap;

/**
 * @author Michael Stover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DBActionList extends HashMap
{
	private static final long serialVersionUID = 1;
   
   public DBActionList(String action){
      this();
      addRequest(action);
   }
   
   public DBActionList(Object a,String action)
   {
   	this(a);
   	addRequest(action);
   }
   
   public DBActionList(Object a,String[] actions)
   {
   	this(actions);
   	setActor(a);
   }
   
   public DBActionList(Object a)
   {
      this();
      put("actor",a);
   }
   
   public DBActionList(String[] actions){
      this();
      for (int i = 0; i < actions.length; i++)
      {
        addRequest(actions[i]);  
      }
   }
   public boolean isRequested(String key){
      try
      {
         Boolean requested = (Boolean)get(key);
         if(requested != null){
            return requested.booleanValue();
         }
      }catch(Exception e){}
      return false;
   }
   
   public void addRequest(String key){
      put(key,new Boolean(true));
   }
   
   /**
    * 
    */
   public DBActionList()
   {
      super();
      put("actions",this);
   }

   /**
    * @param arg0
    */
   public DBActionList(int arg0)
   {
      super(arg0);
      put("actions",this);
   }

   /**
    * @param arg0
    * @param arg1
    */
   public DBActionList(int arg0, float arg1)
   {
      super(arg0, arg1);
      put("actions",this);
   }

   /**
    * @return Returns the actor.
    */
   public Object getActor()
   {
      return get("actor");
   }
   /**
    * @param actor The actor to set.
    */
   public void setActor(Object actor)
   {
      put("actor", actor);
   }
}
