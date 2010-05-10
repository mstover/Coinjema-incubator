/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.cache;


/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CacheService {
    
    public void registerCache(CacheRegistration reg);
    
    public boolean isRegistered(String cacheName);
    
    public boolean isRegistered(Class clazz);
    
    /**
     * If given class is not null, only clears the cache of objects of that type.  Otherwise, if it
     * is null, clears entire cache.
     * @param clazz
     * @throws ServiceException
     */
    public void clear(String cacheName);

    public <T> void clear(Class<T> clazz);
    
    public void clearAll();
    
    public <T> CacheKey<T> checkin(T toCache,long duration);
    
    public <T> T checkout(CacheKey<T> key);
    
    public <T> CacheKey<T> substitute(CacheKey<T> key,T toCache,long duration);
    
    public <T> Cache<T> getCache(String cacheName,Class<? extends T> cacheType);
    
    public <T> Cache<T> getCache(Class<? extends T> clazz);
    
    public <T> T tradeIn(T obj);
}
