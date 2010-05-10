/*
 * Created on Feb 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.TimeConstants;

/**
 * @author mike
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@CoinjemaObject(type="cacheService")
public class DefaultObjectCache implements
        CacheService {

    public static final String REGISTER_CLASS = "registerClass";

    Map<String,Cache> caches = new HashMap<String,Cache>();

    Map<Class<? extends Object>,Cache> classCaches = new HashMap<Class<? extends Object>,Cache>();

    Map<CacheKey<? extends Object>, Object> checkinCache;

    CleanCheckinCache cleaningService;
    
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() {
    	if(cleaningService != null) cleaningService.stopCache();
        checkinCache = Collections
                .synchronizedMap(new HashMap<CacheKey<? extends Object>, Object>());
        start();
    }

    public <T> Cache<T> getCache(String cacheName, Class<? extends T> cacheType) {
		return (Cache<T>)caches.get(cacheName);
	}

    public <T> Cache<T> getCache(Class<? extends T> cacheType) {
		return (Cache<T>)classCaches.get(cacheType);
	}
    
    public <T> T tradeIn(T obj)
    {
    	Cache<T> c = (Cache<T>)getCache(obj.getClass());
    	if(c != null)
    		return (T)c.tradeIn(obj);
    	else return obj;
    }

    public void clear(String cacheName) {
        if (cacheName != null)
            ((Cache) caches.get(cacheName)).clear();
        else {
            Iterator iter = caches.values().iterator();
            while (iter.hasNext()) {
                ((Cache) iter.next()).clear();
            }
        }
    }

    public void clear(Class cacheName) {
        ((Cache) classCaches.get(cacheName)).clear();
    }

    public void clearAll() {
		for(String cacheName : caches.keySet())
		{
			clear(cacheName);
		}	
		for(Class cacheName : classCaches.keySet())
		{
			clear(cacheName);
		}	
	}

	public void addItem(String cacheName, Object newCacheItem) {
        ((Cache) caches.get(cacheName)).addItem(newCacheItem);
    }
    
    public void deleteObject(String cacheName,Object itemToDelete)
    {
    	((Cache)caches.get(cacheName)).deleteObject(itemToDelete);
    }
    
    public void updateObject(String cacheName,Object itemToUpdate)
    {
    	((Cache)caches.get(cacheName)).updateItem(itemToUpdate);
    }
    
    @CoinjemaDependency(method="caches",order=CoinjemaDependency.Order.LAST)
    public void setCaches(Collection<CacheRegistration> registers)
    {
        for(CacheRegistration c : registers)
        {
        	if(c.getCacheName() != null)
        		caches.put(c.getCacheName(),new CacheHelper(c));
        	else 
        		classCaches.put(c.getObjectType(),new CacheHelper(c));
        }
        initialize();
    }
    
    public void registerCache(CacheRegistration reg)
    {
    	caches.put(reg.getCacheName(),new CacheHelper(reg));
    }

    public boolean isRegistered(Class clazz) {
		return classCaches.containsKey(clazz);
	}

	public boolean isRegistered(String cacheName) {
		return caches.containsKey(cacheName);
	}

	public void unRegisterClass(Class clazz) {
        caches.remove(clazz);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Disposable#dispose()
     */
    public void dispose() {
        caches.clear();
        caches = null;
    }

    public <T> CacheKey<T> checkin(T toCache, long duration) {
        CacheKey<T> key = new CacheKey<T>(duration,this);
        checkinCache.put(key, toCache);
        return key;
    }

    public <T> CacheKey<T> substitute(CacheKey<T> key, T toCache, long duration) {
        if (key != null)
            checkinCache.remove(key);
        return checkin(toCache, duration);
    }

    public <T> T checkout(CacheKey<T> key) {
        return (T)checkinCache.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Startable#start()
     */
    public void start() {
        cleaningService = new CleanCheckinCache();
        Thread cleaningthread = new Thread(cleaningService);
        cleaningthread.setDaemon(true);
        cleaningthread.start();

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Startable#stop()
     */
    public void stop() {
        cleaningService.stopCache();
        checkinCache.clear();

    }

    private class CleanCheckinCache implements Runnable {
        private boolean isRunning = true;

        public void stopCache() {
            isRunning = false;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            while (isRunning) {
                List<CacheKey> itemsToRemove = new LinkedList<CacheKey>();
                for (CacheKey key : checkinCache.keySet()) {
                    if (key.isTimeUp()) {
                        itemsToRemove.add(key);
                    }
                }
                for (CacheKey key : itemsToRemove) {
                    checkinCache.remove(key);
                }
                try {
                    Thread.sleep(TimeConstants.MINUTE * 10);
                } catch (Exception e) {
                    getLog().warn("Cache service sleep interrupted", e);
                }
            }
        }

    }
    
    private Logger log;

    @CoinjemaDependency(alias="log4j")
    public void setLog(Logger l)
    {
        log = l;
        if(log.isDebugEnabled())
        	log.debug("Created an object cache service",new Exception());
    }
    
    protected Logger getLog()
    {
        return log;
    }
}
