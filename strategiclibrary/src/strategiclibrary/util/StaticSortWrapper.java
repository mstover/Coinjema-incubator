package strategiclibrary.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class StaticSortWrapper<T> extends SortWrapper<T> {

	transient ArrayList<T> sortable;
	
	public StaticSortWrapper() {
		super();
	}

	public StaticSortWrapper(T[] array)
	{
		super();
		sortable = new ArrayList<T>();
		for(T a : array)
			sortable.add(a);
	}
	
	public StaticSortWrapper(Iterable<? extends T> list)
	{
		super();
		sortable = new ArrayList<T>();
		for(T a : list)
			sortable.add(a);
	}

	@Override
	public Iterator<T> iterator() {
		Collections.sort(sortable,this);
		return sortable.iterator();
	}
}
