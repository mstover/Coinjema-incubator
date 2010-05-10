/*
 * Created on Nov 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.sql.orm;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.Factory;
import org.apache.commons.collections.MultiHashMap;
import org.apache.commons.collections.MultiMap;
import org.apache.commons.collections.map.LazyMap;
import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.util.Converter;

/**
 * @author mike
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@CoinjemaObject
public abstract class AbstractObjectMapping implements ObjectMapping,
		DataRetriever {
	private static SimpleDataRetriever retriever = new SimpleDataRetriever();

	protected Map<String, Map<String, String>> subclassMap = (Map<String, Map<String, String>>) LazyMap
			.decorate(new HashMap<String, String>(), new Factory() {

				public Object create() {
					return new HashMap<String, String>();
				}
			});

	protected String methodName = "";

	String[] primary;

	boolean cachedObject = false;

	boolean isMultiRow = false;

	CacheService cache;

	/**
	 * @param mappings
	 *            The mappings to set.
	 */
	public void setMappings(MultiMap mappings) {
		this.mappings = mappings;
	}

	public void setMappings(Map<String, DataRetriever> mappings) {
		this.mappings = new MultiHashMap();
		this.mappings.putAll(mappings);
	}

	/**
	 * @param subclassMap
	 *            The subclassMap to set.
	 */
	public void setSubclassMap(Map<String, Map<String, String>> subclassMap) {
		this.subclassMap = subclassMap;
	}

	protected MultiMap mappings = new MultiHashMap();

	protected Class objectClass;

	private static Map<Class, Map<String, Method>> methods = new HashMap<Class, Map<String, Method>>();

	protected String name;

	protected ObjectMapSet mapSet;

	public static final String LONG = "LONG";

	public static final String STRING = "STRING";

	/**
	 * Keep a store of methods for classes and make them available by
	 * methodname.
	 * 
	 * @param clazz
	 * @param methodName
	 * @return Method
	 */
	protected Method getMethod(Class clazz, String methodName,
			DataRetriever retriever) {
		Map<String, Method> methodMap = methods.get(clazz);
		if (methodMap == null) {
			methodMap = new HashMap<String, Method>();
			methods.put(clazz, methodMap);
		}
		Method meth = methodMap.get(methodName);
		if (meth == null) {
			Method[] methodList = clazz.getMethods();
			for (int i = 0; i < methodList.length; i++) {
				if (methodName.equals(methodList[i].getName())) {
					methodMap.put(methodList[i].getName(), retriever
							.chooseMethod(methodMap
									.get(methodList[i].getName()),
									methodList[i]));
				}
			}
			meth = methodMap.get(methodName);
		}
		if (meth == null) {
			return methodMap.get(SimpleDataRetriever.PUT_METHOD);
		}
		return meth;
	}

	public Method chooseMethod(Method one, Method two) {
		return retriever.chooseMethod(one, two);
	}

	/**
	 * Based on values in tables, a subclass of the main object mapping object
	 * may be required for a given data row. If not, null is returned.
	 * 
	 * @param data
	 * @param mapping
	 * @return
	 */
	protected ObjectMapping tradeInMapping(Object data) {
		Iterator iter = getSubclassKeys().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			ObjectMapping subclassMap = (ObjectMapping) mapSet
					.getMapping(getSubclassMap(key, Converter.getString(
							retriever.getValueFromStore(data, key), "")));
			if (subclassMap != null) {
				return subclassMap;
			}
		}
		return null;
	}

	protected String getSubclassMap(String key, String value) {
		Map m = null;
		m = (Map) subclassMap.get(key);
		return (String) m.get(value);
	}

	protected Set getSubclassKeys() {
		return subclassMap.keySet();
	}

	/*
	 * public String toString() { return "ObjectMapping{\n\tname='" + name +
	 * "'\n\tmethod='" + methodName + "'\n\tobjectClass='" + objectClass +
	 * "'\n\tmappings = " + mappings + "\n}"; }
	 */

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param methodName
	 *            The methodName to set.
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * @return Returns the methodName.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public DataRetriever put(String key, DataRetriever value) {
		if (mappings == null) {
			mappings = new MultiHashMap();
		}
		return (DataRetriever) mappings.put(key, value);
	}

	/**
	 * @param key
	 * @return
	 */
	protected Collection<DataRetriever> get(String key) {
		if (mappings != null) {
			return (Collection<DataRetriever>) mappings.get(key);
		}
		return null;
	}

	/**
	 * @return Returns the objectClass.
	 */
	public Class getObjectClass() {
		return objectClass;
	}

	/**
	 * Returns an iterator for the methods/properties the mapper defines.
	 * 
	 * @return
	 */
	protected Iterator iterator() {
		return mappings.keySet().iterator();
	}

	public void setObjectMapSet(ObjectMapSet mapSet) {
		this.mapSet = mapSet;
	}

	/**
	 * Generate an object from the data, mapping, and class.
	 * 
	 * @param data
	 * @param objectType
	 * @param mapping
	 * @return
	 * @throws Exception
	 *             Object
	 */
	public Object getObject(Object data, Object target) {
		boolean primaryValueFound = false;
		boolean referenceOnly = false;
		if (target == null) {
			ObjectMapping mapping = tradeInMapping(data);
			if (mapping != null) {
				return mapping.getObject(data, target);
			}
		}
		if (objectClass.equals(String.class)) {
			target = new String(Converter.getString(retriever
					.getValueFromStore(data, STRING)));
			primaryValueFound = true;
			referenceOnly = false;
		} else if (objectClass.equals(Long.class)) {
			target = new Long(Converter.getLong(retriever.getValueFromStore(
					data, "ID")));
			referenceOnly=false;
			primaryValueFound = true;
		} else {
			referenceOnly = true;
			Iterator propIter = iterator();
			while (propIter.hasNext()) {
				String methodName = (String) propIter.next();
				for (DataRetriever column : get(methodName)) {
					Method meth = getMethod(objectClass, methodName, column);
					Object arg[] = column.retrieveData(data, methodName, meth);
					if (arg != null && arg.length > 0 && arg[0] != null) {
						if (referenceOnly && (!isCachedObject() || !(column  instanceof SimpleDataRetriever) ||
								!inPrimaries(((SimpleDataRetriever)column).getKey()))) {
							referenceOnly = false;
						}
						primaryValueFound = true;
						target = setValuesInTarget(target, meth, arg);
					}
				}
			}
		}
		if (primaryValueFound) {
			if (referenceOnly) {
				return cache.tradeIn(target);
			} else {
				return target;
			}
		} else {
			return null;
		}
	}
	
	protected boolean inPrimaries(String key)
	{
		if(key == null || getPrimary() == null) return false;
		for(String prim : getPrimary())
		{
			if(key.equals(prim)) return true;
		}
		return false;
	}

	/**
	 * Get the arguments for setting the value if the value is a compound
	 * property (ie, some custom or arbitrary type that contains multiple
	 * properties of it's own).
	 * 
	 * @param data
	 * @param column
	 * @return
	 * @throws Exception
	 */
	public Object[] retrieveData(Object data, String methodName, Method meth) {
		Object[] arg;
		if (getName() != null) {
			ObjectMapping newMap = (ObjectMapping) mapSet.getMapping(getName());
			arg = new Object[] { newMap.getObject(data, null) };
		} else {
			arg = new Object[] { getObject(data, null) };
		}
		return arg;
	}

	/**
	 * @param target
	 * @param meth
	 * @param arg
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected Object setValuesInTarget(Object target, Method meth, Object[] arg) {
		if (target == null) {
			try {
				target = objectClass.newInstance();
			} catch (Exception e) {
				getLog().error("Problem instantiating object: " + objectClass,
						e);
				throw new RuntimeException(e);
			}
		}
		if (arg != null && arg.length > 0 && arg[arg.length - 1] != null) {
			try {
				meth.invoke(target, arg);
			} catch (Exception e) {
				getLog().error(
						"Problem invoking method: " + meth + " on object: "
								+ target + " with argument: " + arg[0] + "("
								+ arg[0].getClass() + ")", e);
			}
		}
		return target;
	}

	/**
	 * @param objectClass
	 *            The objectClass to set.
	 */
	public void setObjectClass(Class objectClass) {
		this.objectClass = objectClass;
	}

	public void addSubclassMapping(String key, String value,
			String subclassMapFile) {
		Map<String, String> m = subclassMap.get(key);
		m.put(value, subclassMapFile);
	}

	public void addMapping(ObjectMapping subMap) {
		mappings.put(subMap.getMethodName(), subMap);
	}

	public Object getObject(Object query) {
		return getObject(query, null);
	}

	private Logger log;

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

	protected Logger getLog() {
		return log;
	}

	public String[] getPrimary() {
		return primary;
	}

	public void setPrimary(String[] primary) {
		this.primary = primary;
	}

	public boolean isMultiRow() {
		return isMultiRow;
	}

	public void setMultiRow(boolean isMultiRow) {
		this.isMultiRow = isMultiRow;
	}

	public void setMapSet(ObjectMapSet mapSet) {
		this.mapSet = mapSet;
	}

	public boolean isCachedObject() {
		return cache != null && cachedObject;
	}

	public void setCachedObject(boolean cachedObject) {
		this.cachedObject = cachedObject;
	}

	@CoinjemaDependency(method = "cacheService", type = "cacheService", hasDefault = true)
	public void setCache(CacheService c) {
		cache = c;
	}

}
