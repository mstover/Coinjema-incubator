/*
 * Created on Jan 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.nontest.service.sql.mock;

import java.util.Calendar;

/**
 * @author ano ano
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TimeSpent
{   
   long id = -1;
   String objectType;
   PMObject targetObject;
   Calendar date;
   Contact owner;
   float hours = 0;
   String timedObjectType;
   long objectId = -1;
   String description;
   /**
    * 
    */
   public TimeSpent()
   {
      super();
      setObjectType("TimeSpent");
   }

   /**
    * @param id
    * @param displayName
    */
   public TimeSpent(long id, String displayName)
   {
      setId(id);
      setDescription(displayName);
      setObjectType("TimeSpent");
   }
   
   public TimeSpent(Calendar d,Contact o,PMObject target,float hrs)
   {
      this();
      owner = o;
      date = d;
      targetObject = target;
      setTimedObjectType(target.getObjectType());
      setObjectId(target.getId());
      hours = hrs;
   }

   /* (non-Javadoc)
    * @see strategiclibrary.service.sql.DomainObject#getDisplayName()
    */
   public String getDisplayName()
   {
      if(getTargetObject() != null)
      {
         return getTargetObject().getDisplayName();
      }
      else
      {
         return description;
      }
   }

   /**
    * @return Returns the date.
    */
   public Calendar getDate()
   {
      return date;
   }

   /**
    * @param date The date to set.
    */
   public void setDate(Calendar date)
   {
      this.date = date;
   }

   /**
    * @return Returns the hours.
    */
   public float getHours()
   {
      return hours;
   }

   /**
    * @param hours The hours to set.
    */
   public void setHours(float hours)
   {
      this.hours = hours;
   }

   /**
    * @return Returns the owner.
    */
   public Contact getOwner()
   {
      return owner;
   }

   /**
    * @param owner The owner to set.
    */
   public void setOwner(Contact owner)
   {
      this.owner = owner;
   }

   /**
    * @return Returns the targetObject.
    */
   public PMObject getTargetObject()
   {
      return targetObject;
   }

   /**
    * @param targetObject The targetObject to set.
    */
   public void setTargetObject(PMObject targetObject)
   {
      this.targetObject = targetObject;
   }

   /**
    * @return Returns the objectId.
    */
   public long getObjectId()
   {
      return objectId;
   }

   /**
    * @param objectId The objectId to set.
    */
   public void setObjectId(long objectId)
   {
      this.objectId = objectId;
   }

   /**
    * @return Returns the objectType.
    */
   public String getTimedObjectType()
   {
      return timedObjectType;
   }

   /**
    * @param objectType The objectType to set.
    */
   public void setTimedObjectType(String objectType)
   {
      this.timedObjectType = objectType;
   }

   /* (non-Javadoc)
    * @see java.lang.Comparable#compareTo(java.lang.Object)
    */
   public int compareTo(Object o)
   {
      if(o instanceof TimeSpent)
      {
         TimeSpent ts = (TimeSpent)o;
         if(date.before(ts.date))
         {
            return -1;
         }
         else if(date.after(ts.date))
         {
            return 1;
         }
         else
         {
            return 0;
         }            
      }
      return -1;
   }
   /* (non-Javadoc)
    * @see java.lang.Object#equals(java.lang.Object)
    */
   public boolean equals(Object o)
   {
      if(o instanceof TimeSpent)
      {
         TimeSpent comp = (TimeSpent)o;
         if(getId() > 0 && comp.getId() > 0)
         {
            return getId() == comp.getId();
         }
         else
         {
            if(getDate() == null || comp.getDate() == null)
            {
               return false;
            }
            else if(getTimedObjectType() == null || comp.getTimedObjectType() == null)
            {
               return false;
            }
            else if(getObjectId() > 0 && comp.getObjectId() > 0)
            {
               return getDate().equals(comp.getDate()) && 
                     getObjectId() == comp.getObjectId() && 
                     getTimedObjectType().equals(comp.getTimedObjectType());
            }
            else
            {
               return getDate().equals(comp.getDate()) && 
                     getTimedObjectType().equals(comp.getTimedObjectType()) && 
                     getDisplayName().equals(comp.getDisplayName());
            }
         }
      }
      return false;
   }
   /* (non-Javadoc)
    * @see java.lang.Object#hashCode()
    */
   public int hashCode()
   {
      int hash = 0;
      if(getId() > 0)
      {
         hash += getId();
      }
      if(getDate() != null)
      {
         hash += getDate().hashCode();
      }
      hash *= getTimedObjectType().hashCode();
      return hash;
   }

/**
 * @return Returns the id.
 */
public long getId() {
    return id;
}

/**
 * @param id The id to set.
 */
public void setId(long id) {
    this.id = id;
}

/**
 * @return Returns the objectType.
 */
public String getObjectType() {
    return objectType;
}

/**
 * @param objectType The objectType to set.
 */
public void setObjectType(String objectType) {
    this.objectType = objectType;
}

/**
 * @return Returns the description.
 */
public String getDescription() {
    return description;
}

/**
 * @param description The description to set.
 */
public void setDescription(String description) {
    this.description = description;
}
}
