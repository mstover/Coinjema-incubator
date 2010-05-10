package strategiclibrary.service.webaction;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections.MultiMap;
import org.apache.commons.fileupload.FileItem;

import strategiclibrary.service.cache.CacheKey;
import strategiclibrary.util.Converter;
import strategiclibrary.util.TimeConstants;

/**
 * @author Michael Stover
 * @version 1.0
 */
public class MapHandlerData implements HandlerData {

    private MultiMap requestParams;
    
    private Map<String,Object> requestScope;

    private Map<String,Serializable> userScope;
	
	private LinkedList<String> flashNames = new LinkedList<String>();

    private Map<String,Object> appScope;

    private List<String> paramNames;

    private List<String> actionList;

    private Set<String> actionSet;

    private MultiMap uploadParams;

    boolean isFileUpload = false;

    public MapHandlerData(MultiMap request, Map<String,Serializable> session,
            Map<String,Object> context) {
        isFileUpload = false;
        requestParams = request;
        userScope = session;
        appScope = context;
        requestScope = new HashMap<String,Object>();
    }

    public Object getBean(String key) {
        Object bean = getRequestBean(key);
        if (bean == null || bean instanceof String) {
            bean = getUserBean(key);
            if (bean == null) {
                bean = getAppBean(key);
            }
        }
        return bean;
    }

    public void setAppBean(String key, Object bean) {
        if (bean == null)
            appScope.remove(key);
        else
            appScope.put(key, bean);
	}

	public void setFlashBean(String key, Serializable bean) {
        if (bean == null)
            userScope.remove(key);
        else {
        	setUserBean(key,bean);
        	addFlashName(key);
        }
	}
	
	public void clearFlash()
	{
		for(String name : flashNames)
		{
			removeUserBean(name);
		}
		flashNames.clear();
	}

	public String getRequestMessage() {
		return requestScope.toString();
	}

	public void addPermCookie(String key, String value, String domain, String path, boolean secure) {
		// TODO Auto-generated method stub
		
	}

	public String getCookie(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void removeCookie(String key)
	{
		
	}

	public void flashErrorRequest() {
		setFlashBean("preErrorUrl",requestScope.toString());		
	}
	
	public void addFlashName(String name)
	{
		flashNames.add(name);
	}

	public void setRequestBean(String key, Object bean) {
        if (bean == null)
            requestScope.remove(key);
        else
            requestScope.put(key, bean);
	}

	public void setUserBean(String key, Serializable bean) {
        if (bean == null)
            userScope.remove(key);
        else
            userScope.put(key, bean);
	}

    /**
     * Parses the current request for action commands. Sorts the commands into
     * the proper order prior to delivering the list.
     * 
     * @return List
     */
    public List<String> getActions() {
        if (actionList == null) {
            SortedMap<String, String[]> actions = new TreeMap<String, String[]>();
            addActions(actions, getParamNames());
            addActions(actions, requestScope.keySet());
            addOrderedActions(actions, getParameter("ServletPath").split("(/|\\.)"));
            actionList = new LinkedList<String>();
            actionSet = new HashSet<String>();
            for (String[] item : actions.values()) {
                for (String action : item) {
                    if (!actionSet.contains(action)) {
                        actionList.add(action);
                        actionSet.add(action);
                    }
                }
            }
        }
        return actionList;
    }

    public void setActions(List a) {
        actionList = a;
    }

    private void addActions(Map<String,String[]> actions, Iterable<String> paramNames) {
        for(String name : paramNames) {
            if (name.startsWith(REQUEST_ACTION)) {
                actions.put(name, getParameterValues(name));
            }
        }
    }

    private void addActions(Map<String,String[]> actions, Collection<String> paramNames) {
        for (String name : paramNames) {
            if (name.startsWith(REQUEST_ACTION)) {
                actions.put(name, getParameterValues(name));
            }
        }
    }

    private void addOrderedActions(Map<String,String[]> actions, String[] actionNames) {
        actions.put("z_actions", actionNames);
    }

    /**
     * Determine whether the given key exists in the HandlerData context.
     * 
     * @param key
     * @return boolean
     */
    public boolean hasParam(String key) {
        return (requestParams.containsKey(key))
                || (requestScope.containsKey(key.toLowerCase()))
                || requestScope.containsKey(key)
                || requestScope.containsKey(key.toLowerCase());
    }

    /**
     * Get a list of all the request parameters held by this HandlerData.
     * 
     * @return Collection
     */
    public Collection<String> getParamNames() {
        if (paramNames == null) {
            paramNames = new LinkedList<String>();
            if (!isFileUpload) {
                for (Object name : requestParams.keySet()) {
                    paramNames.add((String)name);
                }
            } else {
                Iterator iter = uploadParams.keySet().iterator();
                while (iter.hasNext()) {
                    paramNames.add((String) iter.next());
                }
            }
            for (String name : requestScope.keySet()) {
                if (requestScope.get(name) instanceof String) {
                    paramNames.add(name);
                }
            }
        }
        return paramNames;
    }

    /**
     * Get a user session string value. Should only be used for integration with
     * other libraries that populate the session with values.
     * 
     * @param key
     * @return
     */
    public String getUserValue(String key) {
        Object param = userScope.get(key);
        if (param != null) {
            return param.toString();
        } else {
            return null;
        }
    }

    protected String getUploadParam(String key) {
        Collection values = (Collection) uploadParams.get(key);
        if (values == null || values.size() == 0
                || values.iterator().next() instanceof FileItem) {
            return null;
        } else {
            return values.iterator().next().toString();
        }
    }

    protected String[] getUploadParamValues(String key) {
        Collection values = (Collection) uploadParams.get(key);
        if (values == null || values.size() == 0
                || values.iterator().next() instanceof FileItem) {
            return null;
        } else {
            String[] vals = new String[values.size()];
            Iterator iter = values.iterator();
            int count = 0;
            while (iter.hasNext()) {
                vals[count++] = (String) iter.next();
            }
            return vals;
        }
    }

    /**
     * Get the value of a request parameter.
     * 
     * @param key
     * @return String
     */
    public String getParameter(String key) {
        String param = null;
        if(requestParams.containsKey(key))
        {
            Collection c = (Collection)requestParams.get(key);
            if(c.size() > 0) param = (String)c.iterator().next();
        }
        if (param == null) {
            if(requestParams.containsKey(key.toLowerCase()))
            {
                Collection c = (Collection)requestParams.get(key.toLowerCase());
                if(c.size() > 0) param = (String)c.iterator().next();
            }
            if (param == null) {
                try {
                    param = (String) requestScope.get(key);
                    if (param == null) {
                        param = (String) requestScope.get(key
                                .toLowerCase());
                    }
                } catch (ClassCastException e) {
                    // no need to do anything, if it's not a string it wasn't a
                    // "parameter"
                }
            }
        }
        return param;
    }

    /**
     * Get the value of a request parameter. If not present, return default
     * value.
     * 
     * @param key
     * @param defaultValue
     * @return String
     */
    public String getParameter(String key, String defaultValue) {
        String param = getParameter(key);
        if (param == null) {
            return defaultValue;
        } else {
            return param;
        }
    }

    /**
     * Returns the parameter value converted to an integer. If value is null or
     * not a number, the default value will be returned;
     */
    public int getParameterAsInt(String key, int defaultValue) {
        String param = getParameter(key);
        if (param == null) {
            return defaultValue;
        }
        return Converter.getInt(param, defaultValue);
    }

    protected boolean getBooleanValue(Object param, boolean defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        if (param instanceof Boolean) {
            return ((Boolean) param).booleanValue();
        } else {
            return new Boolean(param.toString()).booleanValue();
        }
    }

    /**
     * Get a list of parameter values given a key.
     * 
     * @param key
     * @return String[]
     */
    public String[] getParameterValues(String key) {
        String[] params = null;
        if(requestParams.containsKey(key))
        {
            Collection c = (Collection)requestParams.get(key);
            params = (String[])c.toArray(new String[c.size()]);
        }
        if (params == null) {
            try {
                Object values = requestScope.get(key);
                if (values instanceof String) {
                    params = new String[] { (String) values };
                } else if (values instanceof String[]) {
                    params = (String[]) values;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterValuesFromPartialKey(java.lang.String)
     */
    public Collection<String> getParamNames(String key) {
        Collection<String> values = new LinkedList<String>();
        for (String name : getParamNames()) {
            if (name.indexOf(key) > -1) {
                values.add(name);
            }
        }
        return values;
    }

    public Object getAppBean(String beanName) {
		return appScope.get(beanName);
	}

	public Serializable getFlashBean(String beanName) {
		return getUserBean(beanName);
	}

	public Object getRequestBean(String beanName) {
		return requestScope.get(beanName);
	}

	public Serializable getUserBean(String beanName) {
		Serializable bean = (Serializable)userScope.get(beanName);
		if(bean instanceof CacheKey)
		{
			return ((CacheKey<Serializable>)bean).get();
		}
		else return bean;
	}
    
    void copyCachedToRequestScope()
    {
        for(String name : userScope.keySet())
        {
            Object val = userScope.get(name);
            if(val instanceof CacheKey)
            {
                if(requestScope.get(name) == null)
                {
                    Object cachedValue = ((CacheKey<Serializable>)val).get();
                    requestScope.put(name,((CacheKey<Serializable>)val).get());
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterAsBool(java.lang.String,
     *      boolean)
     */
    public boolean getParameterAsBool(String key, boolean defaultValue) {
        String param = getParameter(key);
        return getBooleanValue(param, defaultValue);
    }

    public void removeAppBean(String name) {
        appScope.remove(name);
	}

	public void removeBean(String name) {
		removeRequestBean(name);
		removeUserBean(name);
		removeAppBean(name);
	}

	public void removeRequestBean(String name) {
        requestScope.remove(name);
	}

	public void removeUserBean(String name) {
        userScope.remove(name);
	}

	public <T extends Object> T getAppBean(String beanName,Class<T> beanClass) throws ActionException {
    	T bean = (T)getAppBean(beanName);
        if (bean != null) {
            return bean;
        }
        bean = instantiateBean(beanClass);
        setAppBean(beanName, bean);
        return bean;
	}

	public <T extends Serializable> T getFlashBean(String beanName,Class<T> beanClass) throws ActionException {
    	T bean = (T)getUserBean(beanName);
        if (bean != null) {
            return bean;
        }
        bean = instantiateBean(beanClass);
        setFlashBean(beanName, bean);
        return bean;
	}

	public <T extends Object> T getRequestBean(String beanName,Class<T> beanClass) throws ActionException {
    	T bean = (T)getRequestBean(beanName);
        if (bean != null) {
            return bean;
        }
        bean = instantiateBean(beanClass);
        setRequestBean(beanName, bean);
        return bean;
	}

	public <T extends Serializable> T getUserBean(String beanName,Class<T> beanClass) throws ActionException {
        	T bean = (T)getUserBean(beanName);
            if (bean != null) {
                return bean;
            }
            bean = instantiateBean(beanClass);
            setUserBean(beanName,bean);
            return bean;
	}

	private <T> T instantiateBean(Class<T> beanClass) throws ActionException {
		T bean = null;
		try {
		    bean = beanClass.newInstance();
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new ActionException("Couldn't instantiate " + beanClass, e);
		}
		return bean;
	}

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterValuesAsLong(java.lang.String)
     */
    public long[] getParameterValuesAsLong(String key, long def) {
        String[] vals = getParameterValues(key);
        long[] convertedValues = new long[vals.length];
        for (int i = 0; i < vals.length; i++) {
            convertedValues[i] = Converter.getLong(vals[i], def);
        }
        return convertedValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterValuesAsLong(java.lang.String)
     */
    public int[] getParameterValuesAsInt(String key, int def) {
        String[] vals = getParameterValues(key);
        int[] convertedValues = new int[vals.length];
        for (int i = 0; i < vals.length; i++) {
            convertedValues[i] = Converter.getInt(vals[i], def);
        }
        return convertedValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterAsLong(java.lang.String,
     *      long)
     */
    public long getParameterAsLong(String key, long defaultValue) {
        String param = getParameter(key);
        if (param == null) {
            return defaultValue;
        }
        return Converter.getLong(param, defaultValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterAsFloat(java.lang.String,
     *      float)
     */
    public float getParameterAsFloat(String key, float defaultValue) {
        return Converter.getFloat(getParameter(key), defaultValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameter(java.lang.String,
     *      java.lang.String, java.util.Set)
     */
    public String getParameter(String key, String defaultValue, Set badValues) {
        String param = getParameter(key, defaultValue);
        if (badValues.contains(param)) {
            return defaultValue;
        }
        return param;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#hasParam(java.lang.String,
     *      java.util.Set)
     */
    public boolean hasParam(String key, Set badValues) {
        String param = getParameter(key, null, badValues);
        return (param == null) ? false : true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#setParameter(java.lang.String,
     *      java.lang.String)
     */
    public void setParameter(String key, String value) {
        requestScope.put(key, value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterAsBool(java.lang.String,
     *      boolean, java.util.Set)
     */
    public boolean getParameterAsBool(String key, boolean defaultValue,
            Set badValues) {
        String param = getParameter(key);
        if (badValues.contains(param)) {
            return defaultValue;
        }
        return getBooleanValue(param, defaultValue);
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getParameterValuesAsList(java.lang.String)
     */
    public List<String> getParameterValuesAsList(String key) {
        String[] values = getParameterValues(key);
        if (values == null) {
            return null;
        }
        return Arrays.asList(values);
    }

    public Set<String> getParameterValuesAsSet(String key) {
        Set<String> set = new HashSet<String>();
        String[] values = getParameterValues(key);
        if (values == null) {
            return null;
        }
        for (int i = 0; i < values.length; i++) {
            set.add(values[i]);
        }
        return set;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#containsAction(java.lang.String)
     */
    public boolean containsAction(String actionName) {
        if (actionSet != null) {
            return actionSet.contains(actionName);
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getFileData(java.lang.String)
     */
    public InputStream getFileData(String key) {
        try {
            FileItem item = (FileItem) ((Collection) uploadParams.get(key))
                    .iterator().next();
            return item.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFileName(String key) {
        try {
            FileItem item = (FileItem) ((Collection) uploadParams.get(key))
                    .iterator().next();
            String name = item.getName();
            if (name.indexOf("\\") > -1) {
                if (!name.endsWith("\\")) {
                    name = name.substring(name.lastIndexOf("\\") + 1);
                }
            }
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#isFileUpload()
     */
    public boolean isFileUpload() {
        return isFileUpload;
    }

    public void setTimedBean(String name, Serializable bean, long duration) {
	}

	/*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.webaction.HandlerData#getBeanMap()
     */
    public Map<String,Object> getBeanMap() {
        Map<String,Object> beanMap = new HashMap<String,Object>();
        for (String name : appScope.keySet()) {
            beanMap.put(name, appScope.get(name));
        }
        for (String name : userScope.keySet()) {
            beanMap.put(name, userScope.get(name));
        }
        for (String name : requestScope.keySet()) {
            beanMap.put(name, requestScope.get(name));
        }
        return beanMap;
    }
}