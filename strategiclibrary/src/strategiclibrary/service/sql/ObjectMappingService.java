/*
 * Created on Nov 4, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.sql;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;

/**
 * @author ano ano
 * @version $Revision: 1.1 $
 */
public interface ObjectMappingService
{

    /**
     * Execute a query, and populate objects of the given type with the results, using the mapping 
     * to translate from SQL ResultSet to the object properties.
     * @param queryName
     * @param objectType
     * @param mappingName
     * @return
     * Collection
     */
    Collection getObjects(String queryName,Map queryValues);
    
    /**
     * Execute a query, and populate objects of the given type with the results, using the mapping 
     * to translate from SQL ResultSet to the object properties.  Also will sort the collection if desired if it's a List type.
     * @param queryName
     * @param queryValues
     * @param sortList
     * @return
     * @throws ServiceException
     */
    Collection getObjects(String queryName,Map queryValues,boolean sortList);
    
    /**
     * Execute a query, and populate objects of the given type with the results, using the mapping 
     * to translate from SQL ResultSet to the object properties.  Puts the objects into a collection of the given 
     * type.  Also will sort the collection if desired if it's a List type.
     * @param queryName
     * @param queryValues
     * @param collectionType
     * @param sortList
     * @return
     * @throws ServiceException
     */
    Collection getObjects(String queryName,Map queryValues,Class collectionType,boolean sortList);
    
    /**
     * Populates a given target object with the data from a query.
     * @param queryName
     * @param queryValues
     * @param target
     * @return true/false for whether an object was successfully retrieved.
     * @throws ServiceException
     */
    boolean getObject(String queryName,Map queryValues,Object target);
    
    /**
     * Convert a store of values (a Map in this case) to an object given the name of a mapping config file.
     * @param values
     * @param mappingName
     * @return
     * @throws ServiceException
     * Object
     */
    Object getObject(Map values,String mappingName);
    

    /**
     * Allows an arbitrary query to be executed and the results returned as an untranslated resultset-type object.
     * @param queryName
     * @param contextValues
     * @return Data
     * @throws ServiceException
     */
    public ResultSet doQuery(String queryName,Map contextValues);
    
    /**
     * Allows an arbitrary update query to be executed.
     * @param queryName
     * @param contextValues
     * @throws ServiceException
     * void
     */
    public int doUpdate(String queryName,Map contextValues);
}
