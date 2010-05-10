package strategiclibrary.nontest;

import java.util.Collection;
import java.util.concurrent.Callable;

import strategiclibrary.nontest.service.sql.mock.Product;
import strategiclibrary.service.cache.DefaultObjectCache;

public class FindProducts implements Callable<Collection<Product>> {
	DefaultObjectCache cache;
	String category;
	String value;
	String family;

	public FindProducts(DefaultObjectCache cache,String family,String category,String value) {
		this.cache = cache;
		this.family = family;
		this.category = category;
		this.value = value;
		
	}

	public Collection<Product> call() throws Exception {
		return cache.getCache(Product.class).getCachedObjects("family",family,category,value);
	}

}
