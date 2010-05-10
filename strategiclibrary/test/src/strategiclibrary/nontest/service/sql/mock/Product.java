package strategiclibrary.nontest.service.sql.mock;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.sql.ObjectMappingService;

public class Product implements Comparable<Product>{
	int id;
	Calendar dateCataloged;
	Map values = new HashMap();
	String primary;

	public String getPrimary() {
		return primary;
	}

	public void setPrimary(String primary) {
		this.primary = primary;
	}

	public Product() {
	}

	public Calendar getDateCataloged() {
		return dateCataloged;
	}

	public void setDateCataloged(Calendar dateCataloged) {
		this.dateCataloged = dateCataloged;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Set getPropertyNames()
	{
		return values.keySet();
	}
	
	public void setValue(String key,Object val)
	{
		values.put(key,val);
	}
	
	public Object getValue(String key)
	{
		return values.get(key);
	}

	public int compareTo(Product o) {
		return id - o.id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Product)
			return id == ((Product)obj).getId();
		else return false;
	}

	@Override
	public int hashCode() {
		return id * 91;
	}
	
	public Collection<Product> findProducts(Map<String,Object> searchValue)
	   {
	      Map<String,Object> values = new HashMap<String,Object>();
	      for(Map.Entry<String,Object> entry : searchValue.entrySet())
	      {
	         if("family".equals(entry.getKey()))
	               values.put("table",entry.getValue());
	         else if("id".equals(entry.getKey()))
	            values.put("id",entry.getValue());
	         else if("Primary".equals(entry.getKey()))
	               values.put("primaryValue",entry.getValue());
	         else if(entry.getValue() instanceof Number)
	         {
	            values.put("category",entry.getKey());
	            values.put("numberValue",entry.getValue());
	         }
	         else if("dateCataloged".equals(entry.getKey()) && entry.getValue() instanceof Calendar)
	               values.put("dateValue",entry.getValue());
	         else if("dateCataloged".equals(entry.getKey()))
	            values.put("dateValueString",entry.getValue());
	         else if("NULL".equals(entry.getKey()) && entry.getValue() != null)
	         {
	            values.put("searchTerm",entry.getValue());
	         }
	         else 
	         {
	            values.put("category",entry.getKey());
	            values.put("value",entry.getValue());
	         }
	      }
	      return (Collection<Product>)getObjectMapper().getObjects("findProducts.sql",values);
	   }
	
	@CoinjemaDynamic(type="objectMappingService")
	public ObjectMappingService getObjectMapper()
	{ return null;}

}
