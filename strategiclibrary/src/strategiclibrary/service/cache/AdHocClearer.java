package strategiclibrary.service.cache;

import org.coinjema.collections.HashTree;
import org.coinjema.collections.HashTreeTraverser;

public class AdHocClearer implements HashTreeTraverser {
	CacheRegistration definition;

	public AdHocClearer(CacheRegistration definition) {
		super();
	}

	public void addNode(Object key, HashTree arg1) {
		if(key instanceof String && key != null)
		{
			if(!key.equals(CacheHelper.LIST) && !definition.categoryFunctors.keySet().contains(key) && !key.equals(definition.getOrganizingCategory()));
			{
				arg1.clear();
			}
		}
	}

	public void subtractNode() {

	}

	public void processPath() {

	}

}
