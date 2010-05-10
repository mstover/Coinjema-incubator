package strategiclibrary.service.cache;

import java.util.Collection;
import java.util.List;

import org.coinjema.context.CoinjemaObject;

@CoinjemaObject(type = "cacheHelper")
public interface Cache<T> {
	
	public void clear();
	
	public Collection<T> getCachedList();
	
	public Collection<T> getCachedList(String category, Object value);
	
	public void addItem(T item);
	
	public void updateItem(T item);
	
	public Collection<T> getCachedObjects(String category, Object value);
	
	public Collection<T> getCachedObjects(Object... categoriesAndValues);
	
	public Collection<T> getCachedObjects(
			List<? extends Object> categoriesAndValues);
	
	public T getCachedObject(String category, Object value);
	
	public T getCachedObject(
			List<? extends Object> categoriesAndValues);
	
	public T getCachedObject(Object... categoriesAndValues);
	
	public void deleteObject(T item);
	
	public Collection<T> tradeIn(Collection<T> objects);

	public T tradeIn(T obj);
}
