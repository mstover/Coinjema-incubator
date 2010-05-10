package strategiclibrary.service.cache;

import java.io.Serializable;

import strategiclibrary.util.TimeConstants;


public class CacheKey<T> implements Serializable {
    private static final long serialVersionUID = 1;
    CacheService service;
    long endTime;
    
    /**
     * Needed for serialization
     *
     */
    public CacheKey()
    {}
    
    
    public CacheKey(CacheService s)
    {
        service = s;
    }

    public CacheKey(long duration,CacheService s) {
        this(s);
        endTime = (System.currentTimeMillis()/TimeConstants.MINUTE) + (duration/TimeConstants.MINUTE);
    }
    
    long getEndTime()
    {
        return endTime;
    }
    
    public T get()
    {
    	if(!isTimeUp())
    	{
    		return service.checkout(this);
    	}
    	else return null;
    }
    
    public boolean isTimeUp()
    {
        long now = (long) System.currentTimeMillis()
                / TimeConstants.MINUTE;
        return getEndTime() < now;
    }
    
    public String toString()
    {
        return "Object removed from session";
    }

}
