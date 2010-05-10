/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.sql;

import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.coinjema.collections.ConfigurationTree;

import strategiclibrary.nontest.AbstractTestCase;
import strategiclibrary.nontest.service.sql.mock.Contact;
import strategiclibrary.nontest.service.sql.mock.Product;
import strategiclibrary.service.sql.orm.ObjectMapSet;
import strategiclibrary.service.sql.orm.ObjectMapping;
import strategiclibrary.service.sql.orm.XmlBasedObjectMapping;
import strategiclibrary.util.Converter;


/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class PackageTest extends AbstractTestCase {

	/**
	 * @param arg0
	 */
	public PackageTest(String arg0) {
		super(arg0);
	}

	public void testObjectMapping() throws Exception {
        System.out.println("About to create mapping service");
		ObjectMappingService mapper = new DefaultMappingService();
        System.out.println("Mapping service created");
		Map values = new HashMap();
		values.put("NAME", "object name");
		values.put("class_name", "strategiclibrary.service.sql.orm.ObjectMapSet");
		values.put("method", "setMapSetter");
		ObjectMapping testObject = (ObjectMapping) mapper.getObject(values,
				"object.map");
		assertEquals("object name", testObject.getName());
		assertEquals(ObjectMapSet.class, testObject.getObjectClass());
		assertEquals("setMapSetter", testObject.getMethodName());

		values.clear();
		values.put("lenient", "true");
		values.put("startDay", "4");
		values.put("year", "104");
		values.put("date", "5");
		values.put("MONTH", "2");
		Calendar result = (Calendar) mapper.getObject(values, "complex.map");
		assertTrue(result.isLenient());
		assertEquals("03/05/2004", Converter.formatDate(result, "MM/dd/yyyy"));
		assertEquals(2, result.get(Calendar.MONTH));
		assertEquals(5, result.get(Calendar.DATE));
		assertEquals(2004, result.get(Calendar.YEAR));
		
		values.clear();
		values.put("CONTACT_FIRSTNAME","Mike");
		values.put("CONTACT_LASTNAME","badValue");
		values.put("MANAGER_EMAIL","manager@lazerinc.com");
		values.put("MANAGER_FIRSTNAME","Ken");
		values.put("MANAGER_LASTNAME","Joyce");
		values.put("TODO_DESCRIPTION","This is my great todo");
		Contact c = (Contact)mapper.getObject(values,"ContactMapper");
		assertEquals("kens@lazerinc.com",c.getEmail());
		assertEquals("Mike",c.getFirstName());
		assertEquals("Stover",c.getLastName());
		assertEquals("Ken",c.getManager().getFirstName());
		assertEquals("This is my great todo",c.getTodo().getDescription());
	}
	
	public void testCompoundMapping() throws Exception
	{
		ObjectMappingService mapper = new DefaultMappingService();
		Map values = new HashMap();
		values.put("prod_id", 5);
		values.put("name","color");
		values.put("val","red");
		values.put("date_cataloged",new GregorianCalendar());
		values.put("num_field","size");
		values.put("num_val",8);
		Product p = (Product)mapper.getObject(values,"product.map");
		assertEquals("red",p.getValue("color"));
		assertEquals(8,((Number)p.getValue("size")).intValue());
	}
}
