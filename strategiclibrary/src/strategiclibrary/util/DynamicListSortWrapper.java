package strategiclibrary.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.coinjema.util.Functor;

public class DynamicListSortWrapper<T> extends SortWrapper<T> {
	
	transient Functor<Collection<? extends T>> dynList;
	
	public DynamicListSortWrapper() {
		super();
	}

	public DynamicListSortWrapper(Functor<Collection<? extends T>> dynamicList)
	{
		this.dynList = dynamicList;
	}

	@Override
	public Iterator<T> iterator() {
		ArrayList<T> list = new ArrayList<T>(dynList.call());
		Collections.sort(list,this);
		return list.iterator();
	}
}
