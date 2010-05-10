/*
 * Created on Feb 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.cache;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.coinjema.util.Functor;
import org.coinjema.util.InvokableWith;

import strategiclibrary.util.TimeConstants;


/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CacheRegistration {
    Class objectType;
    String cacheName;
    InvokableWith mainRetrieval;
    String retrievalSql;
    String updateSql;
    String insertSql;
    String deleteSql;
    InvokableWith addFunctor;
    InvokableWith updateFunctor;
    InvokableWith deleteFunctor;
    Functor adHocRetrieval;
    Functor mapNamesFunctor;
    Functor mappingFunctor;
    Map categoryFunctors;
    Collection processFunctors;
    String organizingCategory;
    Functor organizingFunctor;
    Object[] primaryPath;
    Collection<Functor> primaryFunctors;
    Comparator<? extends Object> comparer;
    Map<String,Object> defaultSearchValues = new HashMap<String,Object>();
    
    /**
     * Process Functors are executed on every cached object as it's retrieved from storage.  
     * Nothing is done with any returned values, if any.  Use these functors to execute any procedures 
     * on the objects that is necessary to initialize them in any way.
     * @return Returns the processFunctors.
     */
    public Collection getProcessFunctors() {
        return processFunctors;
    }
    
    /**
     * Process Functors are executed on every cached object as it's retrieved from storage.  
     * Nothing is done with any returned values, if any.  Use these functors to execute any procedures 
     * on the objects that is necessary to initialize them in any way.
     * @param processFunctors The processFunctors to set.
     */
    public void setProcessFunctors(Collection processFunctors) {
        this.processFunctors = processFunctors;
    }
    long cacheTime = TimeConstants.HOUR;
    
    /**
     * The adhoc retrieval functor is used to retrieve a single object from storage.  It will be invoked and
     * passed a Map of the values to use in retrieval.
     * @return Returns the adHocRetrieval.
     */
    public Functor getAdHocRetrieval() {
        return adHocRetrieval;
    }
    /**
     * @param adHocRetrieval The adHocRetrieval to set.
     */
    public void setAdHocRetrieval(Functor adHocRetrieval) {
        this.adHocRetrieval = adHocRetrieval;
    }
    /**
     * @return Returns the cacheTime.
     */
    public long getCacheTime() {
        return cacheTime;
    }
    /**
     * @param cacheTime The cacheTime to set.
     */
    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }
    
    /**
     * A category functor matches up a category name with a functor that extracts the category
     * value for a given object.  This enables retrieval of lists of objects based on a chosen
     * category and category value.
     * @return Returns the categoryFunctors.
     */
    public Map getCategoryFunctors() {
        return categoryFunctors;
    }
    /**
     * @param categoryFunctors The categoryFunctors to set.
     */
    public void setCategoryFunctors(Map categoryFunctors) {
        this.categoryFunctors = categoryFunctors;
    }
    /**
     * @return Returns the mainRetrieval.
     */
    public InvokableWith getMainRetrieval() {
        return mainRetrieval;
    }
    /**
     * @param mainRetrieval The mainRetrieval to set.
     */
    public void setMainRetrieval(InvokableWith mainRetrieval) {
        this.mainRetrieval = mainRetrieval;
    }
    /**
     * @return Returns the objectType.
     */
    public Class getObjectType() {
        return objectType;
    }
    /**
     * @param objectType The objectType to set.
     */
    public void setObjectType(Class objectType) {
        this.objectType = objectType;
    }
	public Functor getMapNamesFunctor() {
		return mapNamesFunctor;
	}
	
	/**
	 * The MapNamesFunctor returns a collection of objects that will be used as keys to the
	 * mapping functor to retrieve values.  The cached objects will be categorized according to these values.
	 * @param mapNamesFunctor
	 */
	public void setMapNamesFunctor(Functor<Collection> mapNamesFunctor) {
		this.mapNamesFunctor = mapNamesFunctor;
	}
	public Functor getMappingFunctor() {
		return mappingFunctor;
	}
	public void setMappingFunctor(Functor mappingFunctor) {
		this.mappingFunctor = mappingFunctor;
	}

	/*
	 * The name of a category that will be a top-level category that all elements will be categorized under.
	 */
	public String getOrganizingCategory() {
		return organizingCategory;
	}

	public void setOrganizingCategory(String organizingCategory) {
		this.organizingCategory = organizingCategory;
	}

	/**
	 * A functor that will retrieve the top-level category value for a given element.
	 * @return
	 */
	public Functor getOrganizingFunctor() {
		return organizingFunctor;
	}

	public void setOrganizingFunctor(Functor organizingFunctor) {
		this.organizingFunctor = organizingFunctor;
	}

	public Collection<Functor> getPrimaryFunctors() {
		return primaryFunctors;
	}

	public void setPrimaryFunctors(Collection<Functor> primaryFunctors) {
		this.primaryFunctors = primaryFunctors;
	}

	public Object[] getPrimaryPath() {
		return primaryPath;
	}

	public void setPrimaryPath(Object[] primaryPath) {
		this.primaryPath = primaryPath;
	}

	public String getDeleteSql() {
		return deleteSql;
	}

	public void setDeleteSql(String deleteSql) {
		this.deleteSql = deleteSql;
	}

	public String getInsertSql() {
		return insertSql;
	}

	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}

	public String getRetrievalSql() {
		return retrievalSql;
	}

	public void setRetrievalSql(String retrievalSql) {
		this.retrievalSql = retrievalSql;
	}

	public String getUpdateSql() {
		return updateSql;
	}

	public void setUpdateSql(String updateSql) {
		this.updateSql = updateSql;
	}

	public Comparator<? extends Object> getComparer() {
		return comparer;
	}

	public void setComparer(Comparator<? extends Object> comparer) {
		this.comparer = comparer;
	}

	public InvokableWith getAddFunctor() {
		return addFunctor;
	}

	public void setAddFunctor(InvokableWith addFunctor) {
		this.addFunctor = addFunctor;
	}

	public InvokableWith getDeleteFunctor() {
		return deleteFunctor;
	}

	public void setDeleteFunctor(InvokableWith deleteFunctor) {
		this.deleteFunctor = deleteFunctor;
	}

	public InvokableWith getUpdateFunctor() {
		return updateFunctor;
	}

	public void setUpdateFunctor(InvokableWith updateFunctor) {
		this.updateFunctor = updateFunctor;
	}

	public String getCacheName() {
		return cacheName;
	}

	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public Map<String,Object> getDefaultSearchValues() {
		return defaultSearchValues;
	}

	public void setDefaultSearchValues(Map<String,Object> defaultSearchValues) {
		this.defaultSearchValues = defaultSearchValues;
	}
}
