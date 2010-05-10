/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.util;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

/**
 * @author Michael Stover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
@CoinjemaObject
public class CacheTimer implements Serializable
{
	private static final long serialVersionUID = 1;
   private Calendar timer;
   private transient SoftReference cachedObject;
   private long updatePeriod;
   
   transient Logger log;
   
   public static final long HOURLY = 1000 * 60 * 60;
   public static final long HALF_HOUR = 1000 * 60 * 30;
   public static final long DAILY = 1000 * 60 * 60 * 24;
   
   /**
    * 
    */
   public CacheTimer()
   {
      super();
   }
   
   public CacheTimer(long updatePeriod)
   {
      this.updatePeriod = updatePeriod;
   }
   
   @CoinjemaDependency(alias="log4j")
   public void setLogger(Logger l)
   {
	   log = l;
   }
   
   protected boolean needsUpdate()
   {
      if(cachedObject == null) 
      {
         return true;
      }
      if(cachedObject.get() == null)
      {
    	  log.error("Memory limitations caused cache to be dumped");
    	  return true;
      }
      boolean ret = (timer == null || timer.getTime().getTime() < (System.currentTimeMillis() - updatePeriod));
      if(ret) log.info("Cache is scheduled for refresh");
      return ret;
   }
   
   protected void updateTimer()
   {
      timer = new GregorianCalendar();
   }

   /**
    * @return Returns the cachedObject.
    */
   public Serializable getCachedObject()
   {
      if(!needsUpdate())
      {
         return (Serializable)cachedObject.get();
      }
      return null;
   }

   /**
    * @param cachedObject The cachedObject to set.
    */
   public void setCachedObject(Serializable cachedObject)
   {
      this.cachedObject = new SoftReference(cachedObject);
      updateTimer();
   }

   /**
    * @return Returns the updatePeriod.
    */
   public long getUpdatePeriod()
   {
      return updatePeriod;
   }

   /**
    * @param updatePeriod The updatePeriod to set.
    */
   public void setUpdatePeriod(long updatePeriod)
   {
      this.updatePeriod = updatePeriod;
   }
   
   public void clearObject()
   {
      cachedObject = null;
   }

}
