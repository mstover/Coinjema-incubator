package strategiclibrary.service.cache;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.coinjema.collections.HashTree;
import org.coinjema.collections.HashTreeTraverser;
import org.coinjema.collections.SortedHashTree;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

@CoinjemaObject
public class ObjectRemover implements HashTreeTraverser {

	Object item;
	Logger log;
	
	public ObjectRemover(Object item) {
		this.item = item;
	}

	public void addNode(Object arg0, HashTree arg1) {
		if(arg1 != null)
		{
			if(arg1 instanceof SortedHashTree)
			{
				Iterator iter = arg1.list().iterator();
				while(iter.hasNext())
				{
					Object i = iter.next();
					if(item.equals(i))
					{
						iter.remove();
					}
				}
			}
			else
				arg1.remove(item);
		}
	}

	public void subtractNode() {

	}

	public void processPath() {

	}
	
	@CoinjemaDependency(alias="log4j")
	public void setLogger(Logger l)
	{
		log = l;
	}

}
