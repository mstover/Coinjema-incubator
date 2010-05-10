package strategiclibrary.service.template.velocity;

import java.io.StringWriter;
import java.util.HashMap;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.service.template.TemplateService;

public class PackageTest extends AbstractTestCase {

	public PackageTest(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	   
	   public void testRunGroovy() throws Exception
	   {
	      TemplateService velocityService = new VelocityService();
	      StringWriter results = new StringWriter();
	      Object o = velocityService.mergeTemplate("velocitytest.vtl",new HashMap(),results);
	      assertEquals("Hey, did you find this?",results.toString());
	   }

}
