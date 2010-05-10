/*
 * Created on Oct 22, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.nontest.service.sql.mock;

import java.util.Calendar;
import java.util.Collection;


/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class PMObject
{
    long id = -1;
    String objectType;
    private String description;
    private float estimatedHours,actualHours;
    private Contact owner;
    Calendar lastModifiedDate;
    private Collection history;
    String source;
    long sid = -1;
    

    /**
     * 
     */
    public PMObject()
    {
        super();
        setObjectType("PMObject");
    }
    
    public PMObject copyTo(PMObject p)
    {
       p.setDescription(getDescription());
       p.setObjectType(getObjectType());
       p.setOwner(getOwner());
       p.setId(getId());
       p.setActualHours(getActualHours());
       p.setLastModifiedDate(getLastModifiedDate());
       p.setHistory(getHistory());
       p.setDescription(getDescription());
       return p;
    }

    /**
     * @param id
     * @param displayName
     */
    public PMObject(long id, String displayName)
    {
        setId(id);
        setDescription(displayName);
        setObjectType("PMObject");
    }
    
    public PMObject(long id, String displayName,String objectType)
    {
       this(id,displayName);
       setObjectType(objectType);
    }
    
    public PMObject(String ot)
    {
       this();
       setObjectType(ot);
    }

    /**
     * @return
     * String
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param string
     * void
     */
    public void setDescription(String string)
    {
        description = string;
    }
    /**
     * @return
     * float
     */
    public float getActualHours()
    {
        return actualHours;
    }

    /**
     * @return
     * float
     */
    public float getEstimatedHours()
    {
        return estimatedHours;
    }

    /**
     * @param f
     * void
     */
    public void setActualHours(float f)
    {
        actualHours = f;
    }

    /**
     * @param f
     * void
     */
    public void setEstimatedHours(float f)
    {
        estimatedHours = f;
    }

    /**
     * @return
     * Contact
     */
    public Contact getOwner()
    {
        return owner;
    }

    /**
     * @param object
     * void
     */
    public void setOwner(Contact object)
    {
        owner = object;
    }

    /* (non-Javadoc)
     * @see strategiclibrary.taskmaster.service.dbObjects.NamedObject#getDisplayName()
     */
    public String getDisplayName()
    {
        return description;
    }

    /**
     * @return
     * Date
     */
    public Calendar getLastModifiedDate()
    {
        return lastModifiedDate;
    }

    /**
     * @param date
     * void
     */
    public void setLastModifiedDate(Calendar date)
    {
        lastModifiedDate = date;
    }

    /**
     * @return
     * Collection
     */
    public Collection getHistory()
    {
        return history;
    }

    /**
     * @param collection
     * void
     */
    public void setHistory(Collection collection)
    {
        history = collection;
    }

   /**
    * @return Returns the sid.
    */
   public long getSid()
   {
      if(sid < 0)
      {
         return getId();
      }
      return sid;
   }
   /**
    * @param sid The sid to set.
    */
   public void setSid(long sid)
   {
      this.sid = sid;
   }
   /**
    * @return Returns the source.
    */
   public String getSource()
   {
      return source;
   }
   /**
    * @param source The source to set.
    */
   public void setSource(String source)
   {
      this.source = source;
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
}
