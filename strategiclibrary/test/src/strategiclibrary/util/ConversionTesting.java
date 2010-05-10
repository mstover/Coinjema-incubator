package strategiclibrary.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.coinjema.collections.HashTree;
import org.coinjema.collections.ListedHashTree;
import org.coinjema.collections.TreeSorter;
import org.coinjema.util.Functor;

/**
 * @author mstover
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ConversionTesting extends TestCase
{
   public void testGetCalendar() throws Exception
   {
      String date1 = "6/4/10";
      String date2 = "";
      String date3 = "JAN 23, 2001";
      Calendar cal = Converter.getCalendar(date1, null);
      assertNotNull(cal);
      assertEquals(5,cal.get(Calendar.MONTH));
      assertEquals(4,cal.get(Calendar.DATE));
      assertEquals(2010,cal.get(Calendar.YEAR));
      
      assertNull(Converter.getCalendar(date2, null));
      
      cal = Converter.getCalendar(date3, null);
      assertNotNull(cal);
      assertEquals(0,cal.get(Calendar.MONTH));
      assertEquals(23,cal.get(Calendar.DATE));
      assertEquals(2001,cal.get(Calendar.YEAR));
   }
   
   public void testCharConvert() throws Exception
   {
      assertEquals('H',((Character)Converter.convert("H",char.class)).charValue());
      assertEquals('H',((Character)Converter.convert("H",Character.class)).charValue());
   }
   
   public void testGetDate() throws Exception
   {
      String date1 = "6/4/10";
      String date2 = "";
      String date3 = "JAN 23, 2001";
      Calendar cal = new GregorianCalendar();
      cal.setTime(Converter.getDate(date1, null));
      assertNotNull(cal);
      assertEquals(5,cal.get(Calendar.MONTH));
      assertEquals(4,cal.get(Calendar.DATE));
      assertEquals(2010,cal.get(Calendar.YEAR));
      
      assertNull(Converter.getDate(date2, null));
      
      cal.setTime(Converter.getDate(date3, null));
      assertNotNull(cal);
      assertEquals(0,cal.get(Calendar.MONTH));
      assertEquals(23,cal.get(Calendar.DATE));
      assertEquals(2001,cal.get(Calendar.YEAR));
   }
   
   public void testGetClass() throws Exception
   {
      String className = "java.lang.String";
      assertEquals(String.class,Converter.convert(className, Class.class));
   }
   
   public void testBooleanConvert() throws Exception
   {
      assertTrue(((Boolean)Converter.convert("true", boolean.class)).booleanValue());
   }
   
   public void testFloatConvert() throws Exception
   {
      assertEquals(5.0,Converter.getFloat("5.0",0),.001);
   }
   
   public void testFunctor1() throws Exception
   {
      Functor<Integer> f = new Functor<Integer>((Object)"compare","compareTo");
      assertEquals(1,f.invokeWith("beast").intValue());
   }
   
   public void testFunctor2() throws Exception
   {
      Functor<Calendar> f = new Functor<Calendar>("getCalendar");
      assertEquals("java.util.GregorianCalendar",f.invoke(new Converter(),new Object[]{"12/31/2004"}).getClass().getName());
   }
   
   public void testTreeSorting() throws Exception
   {
      HashTree<String> tree = new ListedHashTree<String>();
      tree.add("One","One");
      tree.add("One","Two");
      tree.add("One","Three");
      tree.add("One","Four");
      tree.add("Two","One");
      tree.add("Two","Four");
      tree.add("Two","Three");
      tree.add("Two","Two");
      tree.add("Four","Four");
      tree.add("Four","One");
      tree.add("Four","Two");
      tree.add("Four","Three");
      tree.add("Three","Three");
      tree.add("Three","Two");
      tree.add("Three","Four");
      tree.add("Three","One");
      TreeSorter<String> sorter = new TreeSorter<String>("One");
      tree.traverse(sorter);
      assertEquals("One",tree.getTree("Two").getObject());
      sorter = new TreeSorter<String>();
      tree.traverse(sorter);
      assertEquals("Four",tree.getTree("Two").getObject());
      
   }
   
   public void testTimePeriod1() throws Exception
   {
      TimePeriod tp = new TimePeriod(Converter.getCalendar("2/1/04"),Converter.getCalendar("2/29/04"),"");
      assertEquals(160,tp.getTotalHours());
   }
   
   /**
    * @param arg0
    */
   public ConversionTesting(String arg0) {
      super(arg0);
      // TODO Auto-generated constructor stub
   }
}
