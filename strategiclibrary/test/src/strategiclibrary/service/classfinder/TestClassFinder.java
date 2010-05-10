/*
 * Created on Nov 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.classfinder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.service.classfinder.ClassFinderService;
import strategiclibrary.service.classfinder.DefaultClassFinderService;
import strategiclibrary.service.webaction.WebRequestHandler;


/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestClassFinder extends AbstractTestCase {
	
	/**
	 * @param arg0
	 */
	public TestClassFinder(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	
	public void testGroovyFind() throws Exception
	{
		ClassFinderService finder = new DefaultClassFinderService();
		List clazzes = finder.findClassesThatExtend(new Class[]{WebRequestHandler.class});
		Set classSet = new HashSet();
		Iterator iter = clazzes.iterator();
		while(iter.hasNext())
		{
			classSet.add(((Class)iter.next()).getName());
		}
		assertTrue(classSet.contains("strategiclibrary.service.webaction.TestAction"));
		assertTrue(classSet.contains("strategiclibrary.service.webaction.actions.KeepHistory"));
	}

}
