/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.sql.orm;

import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.collections.MultiHashMap;
import org.coinjema.collections.ConfigurationTree;

;

/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class XmlBasedObjectMapping extends AbstractObjectMapping implements
		ObjectMapping {
	private static final String METHOD = "method";

	public static final String MAP_FILE = "mapfile";

	private static final String CLASS = "class";

	private static final String PAIRS = "pairs";

	private static final String MAPPING = "submappings";

	private static final String SUBCLASS = "subclass";

	private static final String COMPOUND = "compound-mappings";

	public XmlBasedObjectMapping(ConfigurationTree config, ObjectMapSet mapSet)
			throws ClassNotFoundException {
		setObjectMapSet(mapSet);
		configure(config);
	}

	public XmlBasedObjectMapping() {
	}

	protected void copyTo(XmlBasedObjectMapping m) {
		m.name = name;
		m.objectClass = objectClass;
		m.mappings = new MultiHashMap(mappings);
		m.methodName = methodName;
		m.mapSet = mapSet;
	}

	public void configure(ConfigurationTree config)
			throws ClassNotFoundException {
		if (config.getProperty("inherits", null) != null) {
			((XmlBasedObjectMapping) mapSet.getMapping(config
					.getProperty("inherits"))).copyTo(this);
		}
		setMethodName(config.getProperty(METHOD, ""));
		if (config.getProperty(MAP_FILE, "") != "") {
			setName(config.getProperty(MAP_FILE, ""));
			return;
		}
		setMultiRow("true".equals(config.getProperty("multiRow")));
		setPrimary(config.getPropertyNames("primary"));
		setCachedObject(config.getProperty("cached","false").equals("true"));
		configureObjectClass(config.getProperty(CLASS));
		configureSimpleMappings(config.getAsProperties(PAIRS));
		configureCompoundMappings(config.getTree(COMPOUND));
		configureSubMappings(config.getTree(MAPPING));
		configureSubclassMappings(config.getTree(SUBCLASS));
	}

	protected void configureCompoundMappings(ConfigurationTree compoundElements) {
		if (compoundElements != null) {
			for (String name : compoundElements.listPropertyNames()) {
				if(compoundElements.getProperty(MAP_FILE) != null)
				{
					this.put(compoundElements.getValue(name), new CompoundDataRetriever(compoundElements
							.getTree(name),compoundElements.getProperty(MAP_FILE),this.mapSet));
				}
				else
					this.put(compoundElements.getValue(name), new CompoundDataRetriever(compoundElements
						.getTree(name)));
			}
		}
	}

	/**
	 * @param subElements
	 * @throws ClassNotFoundException
	 */
	protected void configureSubMappings(ConfigurationTree subElements)
			throws ClassNotFoundException {
		if (subElements != null) {
			for (String method : subElements.listPropertyNames()) {
				ConfigurationTree subTree = subElements.getTree(method);
				subTree.add(METHOD, method);
				addMapping(new XmlBasedObjectMapping(subTree, mapSet));
			}
		}
	}

	/**
	 * @param props
	 */
	protected void configureSimpleMappings(Properties props) {
		for (Object name : props.keySet()) {
			this.put((String) name, new SimpleDataRetriever(props
					.getProperty((String) name)));
		}
	}

	/**
	 * @param config
	 */
	protected void configureObjectClass(String className) {
		try {
			setObjectClass(Class.forName(className));
		} catch (Exception e) {
			Iterator iter = mapSet.getPackages().iterator();
			while (iter.hasNext()) {
				try {
					setObjectClass(Class.forName(iter.next() + "." + className));
					break;
				} catch (Exception e1) {
					continue;
				}
			}
		}
	}

	protected void configureSubclassMappings(ConfigurationTree configs) {
		if (configs != null) {
			for (String subclass : configs.listPropertyNames()) {
				ConfigurationTree subClassTree = configs.getTree(subclass);
				addSubclassMapping(subClassTree.getValue(null), subclass,
						subClassTree.getProperty(MAP_FILE));
			}
		}
	}
}
