package strategiclibrary.service.cache;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.coinjema.util.Functor;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.nontest.service.sql.mock.Product;

public class CacheTests extends AbstractTestCase {

	public CacheTests(String arg0) {
		super(arg0);
	}
	
	public void testCacheHelper() throws Exception
	{
		CacheHelper helper = new CacheHelper(createRegistration1());
		assertEquals(4,helper.getCachedList().size());
		assertEquals("value3c",((Product)helper.getCachedObjects(new Object[]{"param2","value2c","param3","value3c"}).iterator().next()).getValue("param3"));
	}
	
	public void testMappingFunctors() throws Exception
	{
		CacheHelper helper = new CacheHelper(createRegistration2());
		assertEquals(4,helper.getCachedList().size());
		assertEquals("value2d",((Product)helper.getCachedObject("param1","value1d")).getValue("param2"));
	}
	
	public void testOrganizingCategory() throws Exception
	{
		CacheHelper helper = new CacheHelper(createRegistration3());
		assertEquals(1,helper.getCachedList("param1","value1a").size());
	}
	
	public void testOrganizingCategory2() throws Exception
	{
		CacheHelper helper = new CacheHelper(createRegistration4());
		assertEquals(1,helper.getCachedList("param1","value1a").size());
	}
	
	private CacheRegistration createRegistration1()
	{
		CacheRegistration reg = new CacheRegistration();
		reg.setMainRetrieval(new Functor<Collection<Product>>(new ProductListing(),"list"));
		reg.setCategoryFunctors(getCategoryFunctorMap());
		return reg;
	}
	
	private CacheRegistration createRegistration3()
	{
		CacheRegistration reg = new CacheRegistration();
		reg.setMainRetrieval(new Functor<Collection<Product>>(new ProductListing(),"list"));
		Map f = new HashMap();
		f.put("param2",new Functor("getValue","param2"));
		f.put("param3",new Functor("getValue","param3"));
		reg.setCategoryFunctors(f);
		reg.setOrganizingCategory("param1");
		reg.setOrganizingFunctor(new Functor("getValue","param1"));
		return reg;
	}
	
	private CacheRegistration createRegistration4()
	{
		CacheRegistration reg = new CacheRegistration();
		reg.setMainRetrieval(new Functor<Collection<Product>>(new ProductListing(),"list"));
		reg.setMapNamesFunctor(new Functor<Collection>("getPropertyNames"));
		reg.setMappingFunctor(new Functor("getValue"));
		reg.setOrganizingCategory("param1");
		reg.setOrganizingFunctor(new Functor("getValue","param1"));
		return reg;
	}
	
	private CacheRegistration createRegistration2()
	{
		CacheRegistration reg = new CacheRegistration();
		reg.setMainRetrieval(new Functor<Collection<Product>>(new ProductListing(),"list"));
		reg.setMapNamesFunctor(new Functor<Collection>("getPropertyNames"));
		reg.setMappingFunctor(new Functor("getValue"));
		return reg;
	}
	
	private Map getCategoryFunctorMap()
	{
		Map f = new HashMap();
		f.put("param1",new Functor("getValue","param1"));
		f.put(Arrays.asList(new String[]{"param2","param3"}),new Object());
		f.put("param2",new Functor("getValue","param2"));
		f.put("param3",new Functor("getValue","param3"));
		return f;
	}
	
	public class ProductListing
	{
		List<Product> products;
		public ProductListing()
		{
			products = new LinkedList<Product>();
			Product p = new Product();
			p.setId(1);
			p.setValue("param1","value1a");
			p.setValue("param2","value2a");
			p.setValue("param3","value3a");
			products.add(p);
			 p = new Product();
				p.setId(2);
			p.setValue("param1","value1b");
			p.setValue("param2","value2b");
			p.setValue("param3","value3b");
			products.add(p);
			 p = new Product();
				p.setId(3);
			p.setValue("param1","value1c");
			p.setValue("param2","value2c");
			p.setValue("param3","value3c");
			products.add(p);
			 p = new Product();
				p.setId(4);
			p.setValue("param1","value1d");
			p.setValue("param2","value2d");
			p.setValue("param3","value3d");
			products.add(p);		
			System.out.println(products);
		}
		
		public Collection<Product> list()
		{
			return products;
		}
	}

}
