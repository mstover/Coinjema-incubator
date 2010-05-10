/*
 * Created on Dec 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.template.groovy;

import java.util.HashMap;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.template.groovy.GroovyService;



/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PackageTest extends AbstractTestCase
{
   
   /**
    * @param arg0
    */
   public PackageTest(String arg0)
   {
      super(arg0);
   }
   
   public void testRunGroovy() throws Exception
   {
      TemplateService groovyService = new GroovyService();
      Object o = groovyService.execute("myTestGroovy.groovy",new HashMap());
      assertEquals("Success!!!",o);
   }
}
