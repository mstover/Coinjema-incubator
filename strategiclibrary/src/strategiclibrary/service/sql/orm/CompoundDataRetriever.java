package strategiclibrary.service.sql.orm;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.coinjema.collections.ConfigurationTree;

import strategiclibrary.util.Converter;

public class CompoundDataRetriever extends SimpleDataRetriever {
	String[] keys;

	Map<String, String> filter;

	Map<String, String> keyMap;

	ObjectMapSet mapSet;

	String mapFile;

	public CompoundDataRetriever() {
		super();
	}

	public CompoundDataRetriever(String... keys) {
		this.keys = keys;
	}

	public CompoundDataRetriever(Collection<String> keys) {
		this.keys = new String[keys.size()];
		int count = 0;
		for (String key : keys) {
			this.keys[count++] = key;
		}
	}

	public CompoundDataRetriever(ConfigurationTree props) {
		Collection<String> tempKeys = new LinkedList<String>();
		filter = new HashMap<String, String>();
		for (String key : props.listPropertyNames()) {
			if (props.getProperty(key) == null
					|| props.getProperty(key).length() == 0)
				tempKeys.add(key);
			else
				filter.put(key, props.getProperty(key));
		}
		if (filter.size() == 0)
			filter = null;
		this.keys = tempKeys.toArray(new String[tempKeys.size()]);
	}

	public CompoundDataRetriever(ConfigurationTree props, String mapFile,
			ObjectMapSet mapSet) {
		Collection<String> tempKeys = new LinkedList<String>();
		filter = new HashMap<String, String>();
		boolean preMap = true;
		for (String key : props.listPropertyNames()) {
			if (!key.equals(XmlBasedObjectMapping.MAP_FILE)) {
				if (preMap) {
					if (props.getProperty(key) == null
							|| props.getProperty(key).length() == 0)
						tempKeys.add(key);
					else
						filter.put(key, props.getProperty(key));
				} else {
					if (props.getProperty(key) != null
							&& props.getProperty(key).length() > 0)
						keyMap.put(key, props.getProperty(key));
					tempKeys.add(key);
				}
			} else {
				keyMap = new HashMap<String, String>();
				preMap = false;
			}
		}
		if (filter.size() == 0)
			filter = null;
		this.keys = tempKeys.toArray(new String[tempKeys.size()]);
		this.mapSet = mapSet;
		this.mapFile = mapFile;
	}

	@Override
	public Object[] retrieveData(Object dataStore, String methodName,
			Method meth) {
		Object[] args = new Object[keys.length];
		if (filter != null) {
			for (String key : filter.keySet()) {
				if (!filter.get(key).equals(getValueFromStore(dataStore, key)))
					return null;

			}
		}
		for (int i = 0; i < args.length; i++) {
			args[i] = getValueFromStore(dataStore, keys[i]);
			if (args[i] == null)
				return null;
			args[i] = (args[i] == null) ? null : Converter.convert(args[i],
					meth.getParameterTypes()[i]);
		}
		if (mapFile != null) {
			ObjectMapping newMap = (ObjectMapping) mapSet.getMapping(mapFile);
			Map<String, Object> miniMap = new HashMap<String, Object>();
			int count = 0;
			for (String key : keys) {
				miniMap.put(keyMap.get(key), args[count++]);
			}
			Object retVal = newMap.getObject(miniMap, null);
			return new Object[] { retVal };
		}
		return args;
	}

	@Override
	public Method chooseMethod(Method one, Method two) {

		if (one == null) {
			return two;
		} else if (two == null) {
			return one;
		} else if (one.getParameterTypes().length == keys.length
				&& numCoreObjects(one) > numCoreObjects(two)) {
			return one;
		} else if (two.getParameterTypes().length == keys.length
				&& numCoreObjects(two) > numCoreObjects(one)) {
			return two;
		} else if (one.getParameterTypes().length == keys.length
				&& parametersNotPrimitive(one)) {
			return one;
		} else if (two.getParameterTypes().length == keys.length
				&& parametersNotPrimitive(two)) {
			return two;
		} else if (one.getParameterTypes().length == keys.length) {
			return one;
		} else if (two.getParameterTypes().length == keys.length) {
			return two;
		} else
			return one;
	}

	private boolean parametersNotPrimitive(Method meth) {
		for (Class clazz : meth.getParameterTypes()) {
			if (clazz.isPrimitive())
				return false;
		}
		return true;
	}

	private int numCoreObjects(Method meth) {
		int count = 0;
		for (Class clazz : meth.getParameterTypes()) {
			if (coreClasses.contains(clazz)) {
				count++;
			}
		}
		return count;
	}

	static private Set<Class> coreClasses = new HashSet<Class>();
	static {
		coreClasses.add(String.class);
		coreClasses.add(Number.class);
		coreClasses.add(Integer.class);
		coreClasses.add(Character.class);
		coreClasses.add(Byte.class);
		coreClasses.add(Float.class);
		coreClasses.add(Double.class);
		coreClasses.add(Long.class);
		coreClasses.add(Calendar.class);
		coreClasses.add(java.util.Date.class);
		coreClasses.add(Object.class);
		coreClasses.add(Short.class);
		coreClasses.add(Boolean.class);
		coreClasses.add(Class.class);
	}

}
