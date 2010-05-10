package strategiclibrary.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public abstract class SortWrapper<T> implements Collection<T>,Comparator<T>,Serializable {
	private static final long serialVersionUID = 1;
	
	LinkedList<String> sortProperties = new LinkedList<String>();
	
	public SortWrapper()
	{
		
	}
	
	public void toggleProperty(String property)
	{
		if(sortProperties.remove(property))
		{
			sortProperties.addFirst("-"+property);
		}
		else if(!sortProperties.remove("-" + property))
		{
			sortProperties.addFirst(property);
		}			
	}
	
	protected Iterable<String> getSortProperties()
	{
		return sortProperties;
	}

	public int compare(T o1, T o2) {
		if(o1 == null && o2 == null)
			return 0;
		else if(o1 == null)
			return 1;
		else if(o2 == null)
			return -1;
		else
		{
			for(String property : getSortProperties())
			{
				Object value1 = getPropertyValue(o1,property);
				Object value2 = getPropertyValue(o1,property);
				int ret = compareValues(value1,value2);
				if(ret != 0) return ret;
			}
		}
		return 0;
	}
	
	protected int compareValues(Object o1,Object o2)
	{
		if(o1 == null && o2 == null)
			return 0;
		else if(o1 == null)
			return 1;
		else if(o2 == null)
			return -1;
		else if(o1.getClass().equals(o2.getClass()))
		{
			if(o1 instanceof Comparable && o2 instanceof Comparable)
			{
				return ((Comparable)o1).compareTo(o2);
			}
			else return o1.toString().compareTo(o2.toString());
		}
		return 0;
	}
	
	protected Object getPropertyValue(T o,String property)
	{
		try {
			Method m = o.getClass().getMethod("get" + fixName(property),new Class[0]);
			if(m == null) m = o.getClass().getMethod("is" + fixName(property),new Class[0]);
			if(m == null) return null;
			else return m.invoke(o);
		} catch (Exception e) {
			return null;
		}
	}
	
	protected String fixName(String name)
	{
		if(name == null) return null;
		if(name.length() > 1) return name = name.substring(0,1).toUpperCase() + name.substring(1);
		if(name.length() ==0) return name;
		if(name.length() == 1) return name.toUpperCase();
		return name;
	}

	public boolean add(T o) {
		throw new UnsupportedOperationException();
	}

	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	public abstract Iterator<T> iterator();

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}
}
