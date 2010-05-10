package strategiclibrary.service.webaction;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheKey;
import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

/**
 * @author Michael Stover
 * @version 1.0
 */
@CoinjemaObject(type = "webData")
public class ServletHandlerData implements HandlerData {

	private HttpServletRequest requestScope;

	private HttpServletResponse response;

	private HttpSession userScope;

	private ServletContext appScope;

	private List<String> paramNames;

	private List<String> actionList;

	private Set<String> actionSet;

	private DiskFileUpload uploader;

	private static final String flashNames = "flashNames.list";

	private String cookiePrefix = "stratlib_";

	private MultiMap uploadParams;

	boolean isFileUpload = false;

	private CacheService cacheService;

	public ServletHandlerData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			ServletContext context) {
		isFileUpload = FileUpload.isMultipartContent(request);
		if (isFileUpload) {
			uploader = new DiskFileUpload();
			try {
				List fileItems = uploader.parseRequest(request);
				Iterator iter = fileItems.iterator();
				uploadParams = new MultiHashMap();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						uploadParams.put(item.getFieldName(), item.getString());
					} else {
						uploadParams.put(item.getFieldName(), item);
					}
				}
			} catch (FileUploadException e) {
				getLog().error("Bad upload", e);
			}
		}
		requestScope = request;
		userScope = session;
		appScope = context;
		this.response = response;
		extractCookies();
	}

	public ServletHandlerData(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			ServletContext context, CoinjemaContext cc) {
		this(request, response, session, context);
	}

	public void clearFlash() {
		try {
			log.debug("Flash names = "
					+ getUserBean(flashNames, LinkedList.class));
			for (String name : (List<String>) getUserBean(flashNames,
					LinkedList.class)) {
				removeUserBean(name);
			}
			getUserBean(flashNames, LinkedList.class).clear();
		} catch (ActionException e) { // not gonna happen

		}
	}

	public String getCookie(String key) {
		if (requestScope != null && requestScope.getCookies() != null) {
			for (Cookie cookie : requestScope.getCookies()) {
				if (cookie.getName().equalsIgnoreCase(key))
					return cookie.getValue();
			}
		}
		return null;
	}
	
	private Cookie getCookieObject(String key)
	{
		if (requestScope != null && requestScope.getCookies() != null) {
			for (Cookie cookie : requestScope.getCookies()) {
				if (cookie.getName().equalsIgnoreCase(key))
					return cookie;
			}
		}
		return null;
	}
	
	public void removeCookie(String key)
	{
		Cookie cook = getCookieObject(key);
		cook.setMaxAge(0);		
		response.addCookie(cook);
	}

	public void addFlashName(String name) {
		try {
			log.debug("adding flash name: " + name);
			List<String> names = getUserBean(flashNames, LinkedList.class);
			names.add(name);
		} catch (ActionException e) { // not gonna happen

		}
	}

	public String getRequestMessage() {
		StringBuffer buf = new StringBuffer(requestScope.getRequestURL());
		String queryString = requestScope.getQueryString();
		if (queryString != null && queryString.length() > 0) {
			buf.append("?").append(queryString);
		}
		return buf.toString();
	}

	public void flashErrorRequest() {
		setFlashBean("preErrorUrl", getRequestMessage());
	}

	private void extractCookies() {
		if (requestScope.getCookies() != null) {
			for (Cookie cookie : requestScope.getCookies()) {
				if (cookie.getName().startsWith(cookiePrefix)
						&& cookie.getMaxAge() > 0) {
					requestScope.setAttribute(cookie.getName().substring(
							cookiePrefix.length()), cookie.getValue());
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}

			}
		}
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

	public void setAppBean(String name, Object bean) {
		if (bean == null)
			appScope.removeAttribute(name);
		else
			appScope.setAttribute(name, bean);
	}

	public void setFlashBean(String name, Serializable bean) {
		if (bean == null) {
			userScope.removeAttribute(name);
		} else {
			log.debug("adding flash bean name = " + name + " bean = " + bean);
			setUserBean(name, bean);
			addFlashName(name);
		}
	}

	public void setRequestBean(String name, Object bean) {
		if (bean == null)
			requestScope.removeAttribute(name);
		else
			requestScope.setAttribute(name, bean);
	}

	public void setUserBean(String name, Serializable bean) {
		if (bean == null)
			userScope.removeAttribute(name);
		else
			userScope.setAttribute(name, bean);
	}

	public void setTimedBean(String name, Serializable bean, long duration) {
		try {
			Object value = userScope.getAttribute(name);
			if (value instanceof CacheKey) {
				userScope.setAttribute(name, cacheService.substitute(
						(CacheKey) value, bean, duration));
			} else {
				userScope.setAttribute(name, cacheService.checkin(bean,
						duration));
			}
		} catch (Exception e) {
			throw new RuntimeException("BadCache", e);
		}
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
			addActions(actions, requestScope.getAttributeNames());
			addOrderedActions(actions, requestScope.getServletPath().split(
					"(/|\\.)"));
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

	private void addActions(Map<String, String[]> actions,
			Enumeration<String> paramNames) {
		while (paramNames.hasMoreElements()) {
			String name = paramNames.nextElement();
			getLog().debug("parameter: " + name + " = " + getParameter(name));
			if (name.startsWith(REQUEST_ACTION)) {
				actions.put(name, getParameterValues(name));
			}
		}
	}

	private void addActions(Map<String, String[]> actions,
			Collection<String> paramNames) {
		for (String name : paramNames) {
			getLog().debug("parameter: " + name + " = " + getParameter(name));
			if (name.startsWith(REQUEST_ACTION)) {
				actions.put(name, getParameterValues(name));
			}
		}
	}

	private void addOrderedActions(Map<String, String[]> actions,
			String[] actionNames) {
		actions.put("z_actions", actionNames);
	}

	/**
	 * Determine whether the given key exists in the HandlerData context.
	 * 
	 * @param key
	 * @return boolean
	 */
	public boolean hasParam(String key) {
		return (!isFileUpload ? requestScope.getParameter(key) != null
				: uploadParams.containsKey(key))
				|| (!isFileUpload ? requestScope
						.getParameter(key.toLowerCase()) != null : uploadParams
						.containsKey(key.toLowerCase()))
				|| requestScope.getAttribute(key) != null
				|| requestScope.getAttribute(key.toLowerCase()) != null
				|| requestScope.getHeader(key) != null;
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
				Enumeration paramEnum = requestScope.getParameterNames();
				while (paramEnum.hasMoreElements()) {
					String name = (String) paramEnum.nextElement();
					paramNames.add(name);
				}
			} else {
				Iterator iter = uploadParams.keySet().iterator();
				while (iter.hasNext()) {
					paramNames.add((String) iter.next());
				}
			}
			Enumeration attributeEnum = requestScope.getAttributeNames();
			while (attributeEnum.hasMoreElements()) {
				String name = (String) attributeEnum.nextElement();
				if (requestScope.getAttribute(name) instanceof String) {
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
		Object param = userScope.getAttribute(key);
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
		String param = !isFileUpload ? requestScope.getParameter(key)
				: getUploadParam(key);
		if (param == null) {
			param = !isFileUpload ? requestScope
					.getParameter(key.toLowerCase()) : getUploadParam(key
					.toLowerCase());
			if (param == null) {
				try {
					param = (String) requestScope.getAttribute(key);
					if (getLog().isDebugEnabled()) {
						getLog().debug(
								"getting parameter(" + key
										+ ") from request attributes = "
										+ param);
					}
					if (param == null) {
						param = (String) requestScope.getAttribute(key
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
		String[] params = !isFileUpload ? requestScope.getParameterValues(key)
				: getUploadParamValues(key);
		if (params == null) {
			try {
				Object values = requestScope.getAttribute(key);
				if (values instanceof String) {
					params = new String[] { (String) values };
				} else if (values instanceof String[]) {
					params = (String[]) values;
				}
			} catch (Exception e) {
				getLog()
						.debug(
								"Tried to get a parameter from attributes that wasn't a string",
								e);
			}
			if (params == null) {
				Enumeration headerEnum = requestScope.getHeaders(key);
				List<String> vals = new LinkedList<String>();
				while (headerEnum.hasMoreElements()) {
					String element = (String) headerEnum.nextElement();
					vals.add(element);
				}
				params = vals.toArray(new String[0]);
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
		return appScope.getAttribute(beanName);
	}

	public Serializable getFlashBean(String beanName) {
		return getUserBean(beanName);
	}

	public Object getRequestBean(String beanName) {
		return requestScope.getAttribute(beanName);
	}

	public Serializable getUserBean(String beanName) {
		Serializable bean = (Serializable) userScope.getAttribute(beanName);
		if (bean instanceof CacheKey) {
			return ((CacheKey<Serializable>) bean).get();
		} else
			return bean;
	}

	private Serializable tradeInCacheKey(Serializable bean) {
		if (bean instanceof CacheKey) {
			return ((CacheKey<Serializable>) bean).get();
		} else {
			return bean;
		}
	}

	void copyCachedToRequestScope() {
		Enumeration enumer = userScope.getAttributeNames();
		while (enumer.hasMoreElements()) {
			String name = (String) enumer.nextElement();
			Object val = userScope.getAttribute(name);
			if (val instanceof CacheKey) {
				if (requestScope.getAttribute(name) == null) {
					Object cachedValue = ((CacheKey) val).get();
					requestScope.setAttribute(name, ((CacheKey) val).get());
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
		appScope.removeAttribute(name);
	}

	public void removeBean(String name) {
		removeRequestBean(name);
		removeUserBean(name);
		removeAppBean(name);
	}

	public void removeRequestBean(String name) {
		requestScope.removeAttribute(name);
	}

	public void removeUserBean(String name) {
		userScope.removeAttribute(name);
	}

	public <T extends Object> T getAppBean(String beanName, Class<T> beanClass)
			throws ActionException {
		T bean = (T) getAppBean(beanName);
		if (bean != null) {
			return bean;
		}
		bean = instantiate(beanClass);
		setAppBean(beanName, bean);
		return bean;
	}

	public <T extends Serializable> T getFlashBean(String beanName,
			Class<T> beanClass) throws ActionException {
		T bean = (T) getUserBean(beanName);
		if (bean != null) {
			return bean;
		}
		bean = instantiate(beanClass);
		setFlashBean(beanName, bean);
		return bean;
	}

	public <T extends Object> T getRequestBean(String beanName,
			Class<T> beanClass) throws ActionException {
		T bean = (T) getRequestBean(beanName);
		if (bean != null) {
			return bean;
		}
		bean = (T) instantiate(beanClass);
		setRequestBean(beanName, bean);
		return bean;
	}

	public <T extends Serializable> T getUserBean(String beanName,
			Class<T> beanClass) throws ActionException {
		T bean = (T) getUserBean(beanName);
		if (bean != null) {
			return bean;
		}
		bean = (T) instantiate(beanClass);
		setUserBean(beanName, bean);
		return bean;
	}

	private <T> T instantiate(Class<T> beanClass) throws ActionException {
		T bean = null;
		try {
			bean = beanClass.newInstance();
		} catch (Exception e) {
			getLog().error(
					"Couldn't instantiate response data bean: " + beanClass, e);
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

	/**
	 * Get access to the underlying servlet request object.
	 * 
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return requestScope;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void addPermCookie(String key, String value, String domain,
			String path, boolean secure) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(Integer.MAX_VALUE);
		// cookie.setDomain(domain);
		cookie.setPath(path);
		response.addCookie(cookie);
	}

	public ServletContext getServletContext() {
		return appScope;
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
		requestScope.setAttribute(key, value);

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
			getLog().warn("Key: " + key + " was not a file upload", e);
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
			getLog().warn("Key: " + key + " was not a file upload", e);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.webaction.HandlerData#getBeanMap()
	 */
	public Map<String, Object> getBeanMap() {
		Map<String, Object> beanMap = new HashMap<String, Object>();
		Enumeration appBeans = appScope.getAttributeNames();
		while (appBeans.hasMoreElements()) {
			String name = (String) appBeans.nextElement();
			beanMap.put(name, appScope.getAttribute(name));
		}
		Enumeration userBeans = userScope.getAttributeNames();
		while (userBeans.hasMoreElements()) {
			String name = (String) userBeans.nextElement();
			beanMap.put(name, (Serializable) userScope.getAttribute(name));
		}
		Enumeration requestBeans = requestScope.getAttributeNames();
		while (requestBeans.hasMoreElements()) {
			String name = (String) requestBeans.nextElement();
			beanMap.put(name, requestScope.getAttribute(name));
		}
		return beanMap;
	}

	@CoinjemaDependency(method = "webCacheService")
	public void setCacheService(CacheService service) {
		this.cacheService = service;
	}

	@CoinjemaDependency(method = "cookiePrefix", hasDefault = true)
	public void setCookiePrefix(String cp) {
		cookiePrefix = cp;
	}

	private Logger log;

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

	protected Logger getLog() {
		return log;
	}
}