package strategiclibrary.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.coinjema.util.Functor;

public class DynamicArraySortWrapper<T> extends SortWrapper<T> {

	transient Functor<T[]> dynArray;
	
	public DynamicArraySortWrapper() {
		super();
	}

	public DynamicArraySortWrapper(Functor<T[]> dynamicArray)
	{
		this.dynArray = dynamicArray;		
	}

	@Override
	public Iterator<T> iterator() {
		ArrayList<T> list = new ArrayList<T>();
		for(T a : dynArray.call())
			list.add(a);
		Collections.sort(list,this);
		return list.iterator();
	}
}
