/*
 * Created on Oct 14, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.webaction;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mike
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface HandlerData
{
    public final static String REQUEST_ACTION = "action";

    /**
     * Get a data response bean given the bean's name.  The Request scope will be searched first, followed by the user scope, and then
     * the app scope.
     */
    Object getBean(String beanName);
    
    boolean isFileUpload();
    
    InputStream getFileData(String key);
    
    public String getFileName(String key);

    /**
     * Get a data response bean given the bean's name and the scope where the bean should be found.
     * @param beanName
     * @param scope
     * @return
     */
    Serializable getUserBean(String beanName);
    Object getAppBean(String beanName);
    Object getRequestBean(String beanName);
    Serializable getFlashBean(String beanName);
    void clearFlash();
    
    public void addPermCookie(String key,String value,String domain,String path,boolean secure);
    public String getCookie(String key);
    public void removeCookie(String key);
    
    /**
     * Ask the handler data to save the current request URL that resulted in error as a
     * flash bean.  An application might try to recover the request after dealing with 
     * the problem.
     *
     */
    void flashErrorRequest();

    /**
     * Get a data response bean and if it doesn't currently exist, then make one.  If the bean can't be instantiated, an action
     * exception is thrown.
     * @param beanName
     * @param scope
     * @param instantiate
     * @return
     * Object
     */
    <T extends Serializable> T getUserBean(String beanName,Class<T> beanClass) throws ActionException;
    <T extends Object> T getAppBean(String beanName,Class<T> beanClass) throws ActionException;
    <T extends Object> T getRequestBean(String beanName,Class<T> beanClass) throws ActionException;
    <T extends Serializable> T getFlashBean(String beanName,Class<T> beanClass) throws ActionException;

    /**
     * Set a data response bean into the request, user, or app scope to be made available to other entities.
     * @param name
     * @param bean
     * @param scope
     */
    void setUserBean(String name, Serializable bean);
    void setFlashBean(String name, Serializable bean);
    void setAppBean(String name, Object bean);
    void setRequestBean(String name, Object bean);
    String getRequestMessage();
    
    /**
     * Set a data response bean into the user session for a limited duration of time, after which
     * it will be automatically removed from the session.
     * @param name
     * @param bean
     * @param duration
     */
    void setTimedBean(String name,Serializable bean, long duration);

    /**
     * Remove a bean from the given scope.
     * @param name
     * @param scope
     * void
     */
    void removeUserBean(String name);
    void removeAppBean(String name);
    void removeBean(String name);
    void removeRequestBean(String name);

    /**
     * Get an arbitrary value out of the user scope.
     * @param key
     * @return
     */
    String getUserValue(String key);

    /**
     * Return a list of action commands in sorted order.
     * @return
     */
    List<String> getActions();
    
    void setActions(List a);
    
    /**
     * To determine if a particular action has been requested in the current request.
     * @param actionName
     * @return
     */
    boolean containsAction(String actionName);

    /**
     * Get a request parameter as a string.
     * @param name
     * @return
     */
    String getParameter(String name);

    /**
      * Get the value of a request parameter.  If not present, return default value.
      * @param key
      * @param defaultValue
      * @return
      * String
      */
    public String getParameter(String key, String defaultValue);
    
    /**
     * Get the value of a request parameter.  If not present or has a "bad" value, return the default value.
     * @param key
     * @param defaultValue
     * @param badValues
     * @return
     * String
     */
    public String getParameter(String key,String defaultValue,Set badValues);

    /**
       * Returns the request parameter value converted to an integer.  If value is null or
       * not a number, the default value will be returned;
       */
    public int getParameterAsInt(String key, int defaultValue);
    
    /**
     * Returns the request parameter value converted to a float.  If the value is null or not a number, the
     * default value will be returned.
     * @param key
     * @param defaultValue
     * @return
     * int
     */
    public float getParameterAsFloat(String key,float defaultValue);
    
    /**
          * Returns the request parameter value converted to a long.  If value is null or
          * not a number, the default value will be returned;
          */
       public long getParameterAsLong(String key, long defaultValue);

    /**
       * Returns the request parameter value converted to a boolean.  If value is null or
       * not a number, the default value will be returned;
       */
    public boolean getParameterAsBool(String key, boolean defaultValue);
    
    /**
           * Returns the request parameter value converted to a boolean.  If value is null or
           * not a number, or contained in the "badvalues" set, the default value will be returned;
           */
        public boolean getParameterAsBool(String key, boolean defaultValue,Set<String> badValues);

    /**
    	* Determine whether the given key exists in the HandlerData context.
    	* @param key
    	* @return
    	* boolean
    	*/
    public boolean hasParam(String key);
    
    /**
     * Determine whether the given key exists, discounting the given list of "bad" values.  In other words, even if the
     * parameter exists, if the value of the parameter is one of the "bad" values, then "false" will be returned.
     * @param key
     * @param badValues
     * @return
     * boolean
     */
    public boolean hasParam(String key,Set<String> badValues);

    /**
    	 * Get a list of all the request parameters held by this HandlerData.
    	 * @return
    	 * Collection
    	 */
    public Collection<String> getParamNames();
    
    /**
     * Get all the beans held in this HandlerData in the form of a simple Map, with more specific
     * scoped beans overriding higher scoped beans.
     * @return
     */
    public Map<String,Object> getBeanMap();

    /**
       * Get a list of parameter values given a key.
       * @param key
       * @return
       * String[]
       */
    public String[] getParameterValues(String key);
    
    public Collection<String> getParamNames(String partialKey);
    
    public List<String> getParameterValuesAsList(String key);
    
    public Set<String> getParameterValuesAsSet(String key);

    /**
       * Get a list of parameter values given a key as longs.
       * @param key
       * @param def
       * @return
       * String[]
       */
    public long[] getParameterValuesAsLong(String key, long def);

    /**
           * Get a list of parameter values given a key as ints.
           * @param key
           * @param def
           * @return
           * String[]
           */
    public int[] getParameterValuesAsInt(String key, int def);
    
    public void setParameter(String key,String value);

}
