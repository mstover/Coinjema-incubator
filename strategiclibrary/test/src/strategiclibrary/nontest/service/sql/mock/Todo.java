/*
 * Created on Oct 22, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.nontest.service.sql.mock;

import java.util.Date;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class Todo extends PMObject
{
    String type,comments,devComments,closed,rtTicket;
    Contact requester;
    State state;
    Priority priority;
    float baseLineHours;
    Date estimatedStart,estimatedFinish,actualStart,actualFinish,baseLineStart,baseLineFinish;
    
    public Todo()
    {
        setObjectType("Todo");
    }
    
    /**
     * @return
     * Calendar
     */
    public Date getActualFinish()
    {
        return actualFinish;
    }

    /**
     * @return
     * Calendar
     */
    public Date getActualStart()
    {
        return actualStart;
    }

    /**
     * @return
     * Calendar
     */
    public Date getBaseLineFinish()
    {
        return baseLineFinish;
    }

    /**
     * @return
     * float
     */
    public float getBaseLineHours()
    {
        return baseLineHours;
    }

    /**
     * @return
     * Calendar
     */
    public Date getBaseLineStart()
    {
        return baseLineStart;
    }

    /**
     * @return
     * String
     */
    public String getClosed()
    {
        return closed;
    }

    /**
     * @return
     * String
     */
    public String getComments()
    {
        return comments;
    }

    /**
     * @return
     * String
     */
    public String getDevComments()
    {
        return devComments;
    }

    /**
     * @return
     * Calendar
     */
    public Date getEstimatedFinish()
    {
        return estimatedFinish;
    }
    
    /**
     * @return
     * Calendar
     */
    public Date getEstimatedStart()
    {
        return estimatedStart;
    }

    /**
     * @return
     * NamedObject
     */
    public Priority getPriority()
    {
        return priority;
    }

    /**
     * @return
     * Contact
     */
    public Contact getRequester()
    {
        return requester;
    }

    /**
     * @return
     * String
     */
    public String getRtTicket()
    {
        return rtTicket;
    }

    /**
     * @return
     * NamedObject
     */
    public State getState()
    {
        return state;
    }


    /**
     * @return
     * String
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param calendar
     * void
     */
    public void setActualFinish(Date calendar)
    {
        actualFinish = calendar;
    }

    /**
     * @param calendar
     * void
     */
    public void setActualStart(Date calendar)
    {
        actualStart = calendar;
    }

    /**
     * @param calendar
     * void
     */
    public void setBaseLineFinish(Date calendar)
    {
        baseLineFinish = calendar;
    }

    /**
     * @param f
     * void
     */
    public void setBaseLineHours(float f)
    {
        baseLineHours = f;
    }

    /**
     * @param calendar
     * void
     */
    public void setBaseLineStart(Date calendar)
    {
        baseLineStart = calendar;
    }

    /**
     * @param string
     * void
     */
    public void setClosed(String string)
    {
        closed = string;
    }

    /**
     * @param string
     * void
     */
    public void setComments(String string)
    {
        comments = string;
    }

    /**
     * @param string
     * void
     */
    public void setDevComments(String string)
    {
        devComments = string;
    }

    /**
     * @param calendar
     * void
     */
    public void setEstimatedFinish(Date calendar)
    {
        estimatedFinish = calendar;
    }

    /**
     * @param calendar
     * void
     */
    public void setEstimatedStart(Date calendar)
    {
        estimatedStart = calendar;
    }

    /**
     * @param object
     * void
     */
    public void setPriority(Priority object)
    {
        priority = object;
    }

    /**
     * @param contact
     * void
     */
    public void setRequester(Contact contact)
    {
        requester = contact;
    }

    /**
     * @param string
     * void
     */
    public void setRtTicket(String string)
    {
        rtTicket = string;
    }

    /**
     * @param object
     * void
     */
    public void setState(State object)
    {
        state = object;
    }

    /**
     * @param string
     * void
     */
    public void setType(String string)
    {
        type = string;
    }

   /**
    * @param id
    * @param displayName
    */
   public Todo(long id, String displayName) {
      super(id, displayName);
      setObjectType("Todo");
   }
  
}
