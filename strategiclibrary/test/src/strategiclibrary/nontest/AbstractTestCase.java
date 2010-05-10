/*
 * Created on Oct 21, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.nontest;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;
import org.coinjema.context.ContextFactory;
import org.coinjema.context.source.FileContextSource;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTestCase extends TestCase {
	static boolean configured = false;

	protected String contextRoot = "test/contexts";

	/**
	 * @param arg0
	 */
	public AbstractTestCase(String arg0) {
		super(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		ContextFactory.createRootContext(new FileContextSource(contextRoot));
		if (!configured) {
			PropertyConfigurator.configure("test/contexts/logging4j.properties");
			configured = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		ContextFactory.destroyContext(null);
	}
}
