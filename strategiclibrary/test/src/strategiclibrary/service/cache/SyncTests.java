package strategiclibrary.service.cache;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.nontest.FindProducts;
import strategiclibrary.nontest.service.sql.mock.Product;

public class SyncTests extends AbstractTestCase {

	public SyncTests(String arg0) {
		super(arg0);
	}
	
	public void testSyncAdHoc() throws Exception
	{
		DefaultObjectCache cache = new DefaultObjectCache();
		//assertEquals(792,cache.getCachedList(Product.class,"family","ia_plytx").size());

		ExecutorService threadPool = Executors.newFixedThreadPool(6);
		List<Callable<Collection<Product>>> inits = new LinkedList<Callable<Collection<Product>>>();
		for(int i = 0;i < 20;i++)
		{
			inits.add(new FindProducts(cache,"ia_plytx","Archive","Active"));
		}

		List<Future<Collection<Product>>> results = threadPool.invokeAll(inits);
		int count = 0;
		for(Future<Collection<Product>> f : results)
		{
			System.out.println("count = " + count);
			System.out.println("size = " +f.get().size());
			if(count == 0) count = f.get().size();
			else assertEquals(count,f.get().size());
		}
	}

}
