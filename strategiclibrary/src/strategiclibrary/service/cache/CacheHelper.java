/*
 * Created on May 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.coinjema.collections.HashTree;
import org.coinjema.collections.ListedHashTree;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Functor;

import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.util.CacheTimer;

/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */

public class CacheHelper<T> implements Cache<T> {

	static final long serialVersionUID = 1;

	static final String LIST = "List";

	CacheTimer timer;

	CacheRegistration definition;

	boolean searchingForTradein = false;

	Set<List<? extends Object>> adHocCaches = new HashSet<List<? extends Object>>();

	public CacheHelper() {

	}

	public synchronized void clear() {
		timer.clearObject();
	}

	public CacheHelper(CacheRegistration def) {
		definition = def;
		timer = new CacheTimer(def.getCacheTime());
	}

	public synchronized Collection<T> getCachedList() {
		HashTree tree = refresh();
		return (Collection<T>) tree.list(LIST);
	}

	ObjectMappingService mapper;

	@CoinjemaDependency(method = "objectMapper", type = "objectMappingService", hasDefault = true)
	public void setMapper(ObjectMappingService m) {
		mapper = m;
	}

	public synchronized Collection getCachedList(String category, Object value) {
		HashTree tree = refresh();
		Collection c = (Collection) tree.list(new Object[] { LIST, category,
				value });
		c = syncAdHoc(category, value, c, tree);
		return c;
	}

	/**
	 * @param mapper
	 * @param tree
	 * @return
	 * @throws ServiceException
	 */
	private synchronized HashTree refresh() {
		Collection objectList = null;
		HashTree tree = null;
		tree = (HashTree) timer.getCachedObject();
		if (tree != null) {
			return tree;
		}
		getLog().info("Rebuilding cache for " + definition.cacheName);
		if (mapper != null && definition.getRetrievalSql() != null)
			objectList = mapper.getObjects(definition.getRetrievalSql(),
					new HashMap<String, Object>(definition
							.getDefaultSearchValues()));
		else
			objectList = (Collection) definition.getMainRetrieval().invoke();
		tree = new ListedHashTree();
		timer.setCachedObject(tree);
		populateHashTree(objectList, tree);
		adHocCaches.clear();
		if (getLog().isDebugEnabled())
			getLog().debug("Cache tree looks like: " + tree);
		return tree;
	}

	private void populateHashTree(Collection list, HashTree tree) {
		for (Object item : list) {
			try {
				processItem(item);
				addItemBasics(item, tree);
			} catch (ClassCastException e) {
				getLog().warn("item is a " + item.getClass().getName(), e);
			}
		}
		for (Object item : list) {
			addItemExtras(item, tree);
		}
	}

	private void processItem(Object item) {
		if (definition.getProcessFunctors() != null) {
			Iterator iter = definition.getProcessFunctors().iterator();
			while (iter.hasNext()) {
				Functor f = (Functor) iter.next();
				if (log.isDebugEnabled())
					log.debug("Processing item, exec " + f.getMethodName()
							+ " on item " + item.getClass());
				f.invokeOn(item);
			}
		}
	}

	/**
	 * @param tree
	 * @param item
	 */
	private synchronized boolean addItemToCache(Object item, HashTree tree) {
		processItem(item);
		if (addItemBasics(item, tree)) {
			addItemExtras(item, tree);
			return true;
		} else
			return false;
	}

	public synchronized void addItem(T item) {
		HashTree tree = refresh();
		boolean ret = addItemBasics(item, tree);
		if (ret) {
			if (definition.getAddFunctor() != null)
				definition.getAddFunctor().invokeWith(item);
			addItemExtras(item, tree);
			if (definition.getInsertSql() != null) {
				Map values = new HashMap();
				values.put("obj", item);
				values.putAll(definition.getDefaultSearchValues());
				mapper.doUpdate(definition.getInsertSql(), values);
			}
		} else {
			throw new DuplicateObjectException();
		}
		clearNonUnique(tree);

	}

	private synchronized void reAddItem(Object item, HashTree tree) {

		if (addItemBasics(item, tree)) {
			addItemExtras(item, tree);
		}
		clearNonUnique(tree);
	}

	public synchronized void updateItem(T item) {
		HashTree tree = refresh();
		clearFromCache(item, tree);
		getLog().debug("Cleared " + item + " from cache tree");
		if (definition.getUpdateFunctor() != null)
			definition.getUpdateFunctor().invokeWith(item);
		if (definition.updateSql != null) {
			Map values = new HashMap();
			values.put("obj", item);
			values.putAll(definition.getDefaultSearchValues());
			mapper.doUpdate(definition.updateSql, values);
		}
		reAddItem(item, tree);
	}

	private synchronized void clearNonUnique(HashTree tree) {
		getLog().debug("Clearing adhoc cache paths");
		for (List<? extends Object> path : adHocCaches) {
			Object[] rearranged = rearrangeSearchValues(path
					.toArray(new Object[path.size()]));
			getLog().debug("removing path: " + rearranged);
			tree.remove(rearranged);
		}
	}

	/**
	 * @param item
	 * @param tree
	 */
	private void addItemExtras(Object item, HashTree tree) {
		Object organizingObject = null;
		if (definition.getOrganizingFunctor() != null) {
			organizingObject = definition.getOrganizingFunctor().invokeOn(item);
		}
		processCategoryFunctors(item, tree, organizingObject);
		processMappingFunctor(item, tree, organizingObject);
	}

	private void processMappingFunctor(Object item, HashTree tree,
			Object organizingObject) {
		if (definition.getMapNamesFunctor() != null) {
			for (Object name : (Collection) definition.getMapNamesFunctor()
					.invokeOn(item)) {
				if (organizingObject != null) {
					tree.add(
							new Object[] {
									definition.getOrganizingCategory(),
									name,
									organizingObject,
									definition.getMappingFunctor().invoke(item,
											name) }, item);
				} else
					tree.add(
							new Object[] {
									name,
									definition.getMappingFunctor().invoke(item,
											name) }, item);
			}
		}
	}

	private void processCategoryFunctors(Object item, HashTree tree,
			Object organizingObject) {
		if (definition.getCategoryFunctors() != null) {
			for (Iterator i = definition.getCategoryFunctors().keySet()
					.iterator(); i.hasNext();) {
				Object categories = i.next();
				if (categories instanceof String) {
					Functor f = (Functor) definition.getCategoryFunctors().get(
							categories);
					Object key = f.invokeOn(item);
					if (key != null) {
						if (organizingObject != null) {
							tree.add(new Object[] {
									definition.getOrganizingCategory(),
									categories, organizingObject, key }, item);
						} else
							tree.add(new Object[] { categories, key }, item);
					}
				} else if (categories instanceof Collection) {
					HashTree subTree = tree;
					List<Object> values = new LinkedList<Object>();
					if (organizingObject != null) {
						subTree = subTree.add(definition
								.getOrganizingCategory());
						values.add(organizingObject);
					}
					for (String category : (Collection<String>) categories) {
						subTree = subTree.add(category);
						Functor f = (Functor) definition.getCategoryFunctors()
								.get(category);
						values.add(f.invokeOn(item));
					}
					for (Object value : values) {
						subTree = subTree.add(value);
					}
					subTree.add(item);
				}
			}
		}
	}

	/**
	 * @param item
	 * @return
	 */
	private boolean addItemBasics(Object item, HashTree tree) {
		if (definition.getOrganizingFunctor() != null) {
			Object o = definition.getOrganizingFunctor().invokeOn(item);
			Object[] path = new Object[] { LIST,
					definition.getOrganizingCategory(), o };
			if (tree.getTree(path) == null) {
				tree.set(path, new ListedHashTree());
			} else if (tree.getTree(path).containsKey(item))
				return false;
			tree.add(path, item);
		} else {
			if (tree.getTree(LIST) == null) {
				tree.set(LIST, new ListedHashTree());
			} else if (tree.getTree(LIST).containsKey(item))
				return false;
			tree.add(LIST, item);
		}
		return true;
	}

	protected Collection adHocQuery(HashTree tree,
			Object... categoriesAndValues) {
		return adHocQuery(createList(categoriesAndValues), tree);
	}

	protected Collection adHocQuery(List<? extends Object> categoriesAndValues,
			HashTree tree) {
		Map<String, Object> values = new HashMap<String, Object>(definition
				.getDefaultSearchValues());
		Object category = null;
		Object value = null;
		boolean newObjects = true;
		for (Object cv : categoriesAndValues) {
			if (cv == null)
				return null;
			if (category == null)
				category = cv;
			else if (value == null) {
				value = cv;
				values.put((String) category, value);
				category = null;
				value = null;
			}
		}
		Collection<T> c = null;
		if (definition.getAdHocRetrieval() != null)
			c = (Collection) definition.getAdHocRetrieval().invokeWith(
					new Object[] { values });
		else if (mapper != null && definition.getRetrievalSql() != null)
			c = mapper.getObjects(definition.getRetrievalSql(), values);
		else
			return null;
		if (c != null) {
			Collection<T> retCol = new LinkedList<T>();
			for (T d : c) {
				if (d != null) {
					if (!addItemToCache(d, tree))
						retCol.add(tradeIn(d));
					else
						retCol.add(d);
				}
			}
			c = retCol;
		}
		if (getLog().isDebugEnabled()) {
			getLog().debug("Ran ad hoc query with " + categoriesAndValues);
		}
		if (!newObjects) {
			c = tradeIn(c);
		}
		storeAdHocResults(categoriesAndValues, c, tree);
		return c;
	}

	private synchronized void storeAdHocResults(
			List<? extends Object> categoriesAndValues, Collection c,
			HashTree tree) {
		Object[] rearranged = rearrangeSearchValues(categoriesAndValues
				.toArray());
		if (tree.getTree(rearranged) == null
				|| tree.getTree(rearranged).size() == 0) {
			tree.add(rearranged, c);
			adHocCaches.add(categoriesAndValues);
		}
	}

	public synchronized Collection getCachedObjects(String category,
			Object value) {
		try {
			HashTree tree = refresh();
			Collection c = tree.list(new Object[] { category, value });
			c = syncAdHoc(tree, c, category, value);
			return c;
		} catch (Exception e) {
			getLog().warn(
					"Unable to retrieve object from category: " + category
							+ " and value: " + value, e);
			return null;
		}
	}

	public synchronized Collection getCachedObjects(
			Object... categoriesAndValues) {
		try {
			if (categoriesAndValues.length % 2 == 0) {
				Object[] rearranged = rearrangeSearchValues(categoriesAndValues);
				HashTree tree = refresh();
				Collection c = tree.list(rearranged);
				c = syncAdHoc(tree, c, categoriesAndValues);
				return c;
			} else {
				throw new RuntimeException("Invalid # of arguments");
			}
		} catch (Exception e) {
			getLog().warn(
					"Unable to retrieve object from categories: "
							+ categoriesAndValues, e);
			return null;
		}
	}

	private Collection syncAdHoc(HashTree tree, Collection c,
			Object... categoriesAndValues) {
		if (c == null || c.size() == 0)
			c = adHocQuery(tree, categoriesAndValues);
		return c;
	}

	private Collection syncAdHoc(String category, Object value, Collection c,
			HashTree tree) {
		if (c == null || c.size() == 0
				|| !adHocCaches.contains(createList(category, value))) {
			Collection adHoc = adHocQuery(tree, category, value);
			if (adHoc != null)
				c = adHoc;
		}
		return c;
	}

	private LinkedList<Object> createList(Object... objects) {
		LinkedList<Object> ll = new LinkedList<Object>();
		for (Object o : objects)
			ll.add(o);
		return ll;
	}

	private Object[] rearrangeSearchValues(Object... categoriesAndValues) {
		Object[] rearranged = null;
		if (categoriesAndValues.length == 2)
			rearranged = categoriesAndValues;
		else {
			rearranged = new Object[categoriesAndValues.length];
			int halfLen = (int) (categoriesAndValues.length / 2);
			for (int i = 0, j = 0; i < halfLen; i++, j += 2) {
				rearranged[j] = categoriesAndValues[i];
				rearranged[j + 1] = categoriesAndValues[i + halfLen];
			}
		}
		return rearranged;
	}

	public synchronized Collection getCachedObjects(
			List<? extends Object> categoriesAndValues) {
		return getCachedObjects(categoriesAndValues.toArray());
	}

	public synchronized T getCachedObject(String category, Object value) {
		try {
			HashTree tree = refresh();
			Collection a = getCachedObjects(category, value);
			Object d = null;
			if (a != null && a.size() > 0)
				d = a.iterator().next();
			else if (value != null) {
				a = adHocQuery(tree, category, value);
				if (a != null && a.size() > 0)
					d = a.iterator().next();
			}
			return (T) d;
		} catch (Exception e) {
			getLog().warn(
					"Unable to retrieve object from category: " + category
							+ " and value: " + value, e);
			return null;
		}
	}

	public synchronized T getCachedObject(
			List<? extends Object> categoriesAndValues) {
		try {
			HashTree tree = refresh();
			Collection a = getCachedObjects(categoriesAndValues);
			Object d = null;
			if (a != null && a.size() > 0)
				d = a.iterator().next();
			else if (categoriesAndValues != null) {
				a = adHocQuery(categoriesAndValues, tree);
				if (a != null && a.size() > 0)
					d = a.iterator().next();

			}
			return (T) d;
		} catch (Exception e) {
			getLog().warn("Unable to retrieve object", e);
			return null;
		}
	}

	public synchronized T getCachedObject(Object... categoriesAndValues) {
		try {
			HashTree tree = refresh();
			Collection a = getCachedObjects(categoriesAndValues);
			Object d = null;
			if (a != null && a.size() > 0)
				d = a.iterator().next();
			else if (categoriesAndValues != null) {
				a = adHocQuery(tree, categoriesAndValues);
				if (a != null && a.size() > 0)
					d = a.iterator().next();
			}
			return (T) d;
		} catch (Exception e) {
			getLog().warn("Unable to retrieve object", e);
			return null;
		}
	}

	public void deleteObject(T item) {
		if (definition.getDeleteFunctor() != null)
			definition.getDeleteFunctor().invokeWith(item);
		if (definition.getDeleteSql() != null) {
			Map values = new HashMap();
			values.put("obj", item);
			values.putAll(definition.getDefaultSearchValues());
			mapper.doUpdate(definition.getDeleteSql(), values);
		}
		HashTree tree = refresh();
		clearFromCache(item, tree);
	}

	private synchronized void clearFromCache(Object item, HashTree tree) {
		ObjectRemover remover = new ObjectRemover(item);
		tree.traverse(remover);
	}

	public synchronized Collection<T> tradeIn(Collection<T> objects) {
		try {
			Collection<T> newObjects = (Collection<T>) objects.getClass()
					.newInstance();
			for (T obj : objects)
				newObjects.add(tradeIn(obj));
			return newObjects;
		} catch (Exception e) {
			getLog().warn("Can't trade in these objects " + objects, e);
			return objects;
		}
	}

	public synchronized T tradeIn(T obj) {
		try {
			if (searchingForTradein) {
				return obj;
			}
			searchingForTradein = true;
			Object[] categoriesAndValues = definition.getPrimaryPath();
			if (categoriesAndValues != null) {
				int x = 1;
				for (Functor primary : definition.getPrimaryFunctors()) {
					categoriesAndValues[x] = primary.invokeOn(obj);
					x += 2;
				}
				return getCachedObject(categoriesAndValues);
			} else
				return obj;
		} finally {
			searchingForTradein = false;
		}
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
