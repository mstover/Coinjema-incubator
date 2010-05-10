/*
 * Created on Nov 5, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.sql.orm;

import java.util.Collection;



/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface ObjectMapping extends DataRetriever {
	
	public void setObjectMapSet(ObjectMapSet mapSet);
	
	/**
	 * @return Returns the objectClass.
	 */	
	public Class getObjectClass();

	/**
	 * @param objectClass The objectClass to set.
	 */
	public void setObjectClass(Class objectClass);

	public void addMapping(ObjectMapping subMap);

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	public DataRetriever put(String key, DataRetriever value);

	/**
	 * @return Returns the methodName.
	 */
	public String getMethodName();

	/**
	 * @return Returns the name.
	 */
	public String getName();

	/**
	 * @param name The name to set.
	 */
	public void setName(String name);
	
	/**
	 * Create an object given the raw data.
	 * @param data - Some object that represents raw data to be mapped onto a Java class.
	 * @return
	 */
	public Object getObject(Object data);
	
	/**
	 * Populate an object given the raw data.
	 * @param data - Some object that represents raw data to be mapped onto a Java class.
	 * @return
	 * @throws Exception
	 */
	public Object getObject(Object data,Object target);
	
	/**
	 * Indicates whether the mapping can span multiple rows in the data set.  If so, then
	 * a single object might need to be configured from data from several rows.  A primary
	 * value must be set if true.
	 */
	public boolean isMultiRow();
	
	/**
	 * Provides the value of the primary column name if necessary.  A primary column value can be used
	 * to create a hash of unique objects.
	 */
	public String[] getPrimary();
	
	
}