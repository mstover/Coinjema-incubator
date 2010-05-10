/*
 * Created on Feb 25, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.domain_event;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DomainEvent
{
   private Object source;
   private Object actor;
   private String type;
   /**
    * @return Returns the source.
    */
   public Object getSource()
   {
      return source;
   }
   /**
    * @param source The source to set.
    */
   public void setSource(Object source)
   {
      this.source = source;
   }
   /**
    * @return Returns the type.
    */
   public String getType()
   {
      return type;
   }
   /**
    * @param type The type to set.
    */
   public void setType(String type)
   {
      this.type = type;
   }
   /**
    * @return Returns the actor.
    */
   public Object getActor()
   {
      return actor;
   }
   /**
    * @param actor The actor to set.
    */
   public void setActor(Object actor)
   {
      this.actor = actor;
   }
   /**
    * 
    */
   public DomainEvent() {
      super();
   }
   
   public DomainEvent(Object source,Object actor,String type)
   {
      setSource(source);
      setActor(actor);
      setType(type);
   }
}
