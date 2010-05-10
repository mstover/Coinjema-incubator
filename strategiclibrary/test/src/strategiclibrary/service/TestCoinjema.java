package strategiclibrary.service;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.nontest.UsesLogger;

public class TestCoinjema extends AbstractTestCase {


    public TestCoinjema(String arg0) {
        super(arg0);
    }
    
    public void testLogger() throws Exception
    {
        UsesLogger test = new UsesLogger();
        assertNotNull(test.getLog());
    }

}
