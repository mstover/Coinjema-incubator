/*
 * Created on Nov 4, 2003
 * 
 * To change the template for this generated file go to Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.context.ScriptEvaluator;

import strategiclibrary.service.DataBase;
import strategiclibrary.service.Profiler;
import strategiclibrary.service.sql.orm.ObjectMapSet;
import strategiclibrary.service.sql.orm.ObjectMapping;
import strategiclibrary.service.sql.orm.XmlBasedObjectMapping;

/**
 * This service provides convenient mapping of database query results to Java
 * objects. Sample Configuration: <service
 * role="strategiclibrary.service.sql.ObjectMappingService"
 * class="strategiclibrary.service.sql.DefaultMappingService">
 * <mappingDirectory>
 * 
 * @webapp_path@/WEB-INF/templates </mappingDirectory> <package>
 *                                 strategiclibrary.taskmaster.service.dbObjects
 *                                 </package> </service>
 * 
 * @author Michael Stover
 * @version $Revision: 1.2 $
 */
@CoinjemaObject(type = "objectMappingService")
public class DefaultMappingService implements ObjectMappingService {
	// Mapping configuration constants

	ObjectMapSet mappings;

	DataBase db;

	Profiler prof;;

	public DefaultMappingService() {
	}

	public DefaultMappingService(CoinjemaContext c) {
	}

	public Collection getObjects(String queryName, Map queryValues) {
		return getObjects(queryName, queryValues, ArrayList.class, false);
	}

	public Collection getObjects(String queryName, Map queryValues,
			boolean sortList) {
		return getObjects(queryName, queryValues, ArrayList.class, sortList);
	}

	@CoinjemaDependency(method = "database")
	public void setDatabase(DataBase db) {
		this.db = db;
	}
	
	/*private Collection<Map<String,Object>> getData(ResultSet rs) throws SQLException
	{
		Collection<Map<String,Object>> data = new LinkedList<Map<String,Object>>();
		while(rs.next())
		{
			Map<String,Object> row = new HashMap<String,Object>();
			ResultSetMetaData meta = rs.getMetaData();
			int colCount = meta.getColumnCount()+1;
			for(int i = 1;i < colCount;i++)
			{
				row.put(meta.getColumnName(i).toLowerCase(),rs.getObject(i));
			}
			data.add(row);
		}
		return data;
	}*/
	
	private Object[] getPrimaryKeyValues(ResultSet query,String[] keys) throws SQLException
	{
		Object[] values = new Object[keys.length];
		int i = 0;
		for(String key : keys)
		{
			values[i++] = query.getObject(key);
		}
		return values;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.sql.ObjectMappingService#getObjects(java.lang.String,
	 *      java.lang.Class, java.lang.String)
	 */
	public Collection getObjects(String queryName, Map queryValues,
			Class collectionClass, boolean sortList) {
		queryValues = db.getTemplateContext(queryValues);
		prof.beginTiming(queryName);
		Collection<Object> objects = null;
		ResultSet query = null;
		try {
			ObjectMapping props = (ObjectMapping) mappings
					.getMapping(queryName);
			objects = (Collection<Object>) collectionClass.newInstance();
				query = db.executeTemplateQuery(queryName, db
						.getTemplateContext(queryValues));
				if (props == null) {
					props = mappings.getDefaultMapping(getColumnHeaders(query));
				}
			String[] primaryKeys = props.getPrimary();
			MultiKeyMap hash = null;
			if (props.isMultiRow())
				hash = new MultiKeyMap();
			while (query.next()) {
				Object[] primary = null;
				if (primaryKeys != null) {
					primary = getPrimaryKeyValues(query,primaryKeys);
				}
				if (props.isMultiRow() && hash.containsKey(new MultiKey(primary))) {
					props.getObject(query, hash.get(new MultiKey(primary)));
				} else {
					Object newObj = props.getObject(query);
					objects.add(newObj);
					if (primary != null && props.isMultiRow())
						hash.put(new MultiKey(primary), newObj);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}  finally {
			prof.endTiming(queryName);
			try {
				if (query != null)
					query.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}

		if (sortList && List.class.isAssignableFrom(objects.getClass())) {
			Collections.sort((List) objects);
		}
		return objects;
	}

	String[] getColumnHeaders(ResultSet rs) throws SQLException {
		List<String> headers = new LinkedList<String>();
		ResultSetMetaData rsmd = rs.getMetaData();
		int numHeaders = rsmd.getColumnCount();
		for (int i = 1; i <= numHeaders; i++) {
			headers.add(rsmd.getColumnName(i));
		}
		return headers.toArray(new String[headers.size()]);
	}

	/**
	 * Populates a given object with the data from a query.
	 * 
	 */
	public boolean getObject(String queryName, Map queryValues, Object target) {
		prof.beginTiming(queryName);
		ResultSet query = null;
		try {
			ObjectMapping props = (ObjectMapping) mappings
					.getMapping(queryName);
			query = db.executeTemplateQuery(queryName, db
					.getTemplateContext(queryValues));
			if (props == null) {
				props = mappings.getDefaultMapping(getColumnHeaders(query));
			}
			while (query.next()) {
				props.getObject(query, target);
			}
		} catch (Exception e) {
			throw new RuntimeException("BadSQLQuery", e);
		} finally {
			prof.endTiming(queryName);
			try {
				if (query != null)
					query.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	@CoinjemaDependency(method = "packages")
	public void setPackageNames(Collection<String> packages) {
		this.packageNames = packages;
	}

	private Collection<String> packageNames;

	private String mappingDir;

	private String propFileName = "mapping.properties";

	@CoinjemaDependency(method = "propFile", hasDefault = true)
	public void setPropFileName(String pfn) {
		propFileName = pfn;
	}

	@CoinjemaDependency(method = "mappingDir", order = CoinjemaDependency.Order.LAST)
	public void setMappingDir(String mappingDir) {
		this.mappingDir = mappingDir;
		initialize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.configuration.Configurable#configure(org.apache.avalon.framework.configuration.Configuration)
	 */
	public void initialize() {
		getLog().debug("Initializing object mapping service");
		mappings = new ObjectMapSet(mappingDir);
		mappings.setPackages(packageNames);
		mappings.initMappings();

		String propFile = mappingDir + File.separator + propFileName;
		linkSqlMapFiles(propFile);
		ScriptEvaluator.addEvaluator("orm",new MappedEvaluator());
	}

	private void linkSqlMapFiles(String propFile) {
		Properties matchUpProps = new Properties();
		try {
			matchUpProps.load(new FileInputStream(propFile));
			Iterator iter = matchUpProps.keySet().iterator();
			while (iter.hasNext()) {
				String sqlFile = (String) iter.next();
				String mapFile = matchUpProps.getProperty(sqlFile);
				if (XmlBasedObjectMapping.STRING.equals(mapFile)) {
					ObjectMapping stringMap = new XmlBasedObjectMapping();
					stringMap.setObjectClass(String.class);
					stringMap.setName(sqlFile);
					mappings.addMapping(stringMap);
				} else if (XmlBasedObjectMapping.LONG.equals(mapFile)) {
					getLog().info(
							"sqlFile = " + sqlFile + " mapFile = " + mapFile);
					ObjectMapping longMap = new XmlBasedObjectMapping();
					longMap.setObjectClass(Long.class);
					longMap.setName(sqlFile);
					mappings.addMapping(longMap);
				} else {
					mappings.addMapping(sqlFile, mappings.getMapping(mapFile));
				}
			}
		} catch (IOException e) {
			getLog().error("Unable to open mapping property file.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.taskmaster.service.PMToolBusinessLogicService#doQuery(java.lang.String,
	 *      java.util.Map)
	 */
	public ResultSet doQuery(String queryName, Map contextValues) {
		try {
			return db.executeTemplateQuery(queryName, db
					.getTemplateContext(contextValues));
		} catch (SQLException e) {
			throw new RuntimeException("Bad SQL", e);
		} catch (Exception e) {
			throw new RuntimeException("Bad SQL Template", e);
		}
	}

	public int doUpdate(String queryName, Map contextValues) {
		try {
			return db.executeTemplateUpdate(queryName, db
					.getTemplateContext(contextValues));
		} catch (SQLException e) {
			throw new RuntimeException("Bad SQL", e);
		} catch (Exception e) {
			throw new RuntimeException("Bad SQL Template", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.sql.ObjectMappingService#getObject(java.util.Map,
	 *      java.lang.String)
	 */
	public Object getObject(Map values, String mappingName) {
		ObjectMapping props = (ObjectMapping) mappings.getMapping(mappingName);
		try {
			return props.getObject(values);
		} catch (Exception e) {
			getLog().warn("Problem mapping object", e);
			throw new RuntimeException("Problem mapping object", e);
		}
	}

	private Logger log;

	@CoinjemaDependency(method = "profiler")
	public void setProfiler(Profiler prof) {
		this.prof = prof;
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
        if(log.isDebugEnabled())
        	log.debug("Created a mapping service",new Exception());
	}

	protected Logger getLog() {
		return log;
	}
}
