/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.util;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Michael Stover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TimePeriod implements Serializable
{
	private static final long serialVersionUID = 1;
   private Calendar start,end;
   private String label;
   
   public TimePeriod()
   {
      
   }
   
   public TimePeriod(Calendar s,Calendar e,String l)
   {
      start = s;
      end = e;
      label = l;
   }
   
   public TimePeriod(Calendar s,String l)
   {
      start = s;
      end = (Calendar)start.clone();
      end.add(Calendar.MONTH,1);
      end.add(Calendar.DATE,-1);
      label = l;
   }
   
   public boolean isWithin(Calendar c)
   {
      return (start.before(c) || start.equals(c)) && (end.after(c) || end.equals(c));
   }
   
   public float getRatio(float hours,int numContacts)
   {
      return hours / (getTotalHours() * numContacts);
   }
   
   public int getTotalHours()
   {
      int hours = 0;
      Calendar s = (Calendar)start.clone();
      while(!s.after(end))
      {
         if(isWeekDay(s))
         {
            hours += 8;
         }
         s.add(Calendar.DATE,1);
      }
      return hours;
   }
   
   protected boolean isWeekDay(Calendar s)
   {
      String day = Converter.formatDate(s, "EEE");
      if(!day.equalsIgnoreCase("Sun") && !day.equalsIgnoreCase("Sat"))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * @return Returns the end.
    */
   public Calendar getEnd()
   {
      return end;
   }

   /**
    * @param end The end to set.
    */
   public void setEnd(Calendar end)
   {
      this.end = end;
   }

   /**
    * @return Returns the start.
    */
   public Calendar getStart()
   {
      return start;
   }

   /**
    * @param start The start to set.
    */
   public void setStart(Calendar start)
   {
      this.start = start;
   }

   /**
    * @return Returns the label.
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @param label The label to set.
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

}
