package strategiclibrary.service.sql.orm;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.util.Converter;

@CoinjemaObject
public class SimpleDataRetriever implements DataRetriever {
	Logger log;

	   protected static final String PUT_METHOD = "put";
	String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object[] retrieveData(Object dataStore, String methodName,Method meth) {
	      Object[] arg;
	      Object value = getValueFromStore(dataStore, getKey());
	      if(log.isDebugEnabled()) log.debug("calling method " + methodName + " for value " + value);
	      if(meth == null) log.warn("no method found: calling method " + methodName + " for value " + value);
	      Object argValue = (value == null) ? null : Converter.convert(value,
	            meth.getParameterTypes()[0]);
	      if (argValue == null)
	      {
	         arg = null;
	      }
	      else if (meth.getName().equals(PUT_METHOD))
	      {
	         arg = new Object[] { methodName, value };
	      }
	      else
	      {
	         arg = new Object[] { argValue };
	      }
	      return arg;
	}

	public SimpleDataRetriever() {
		super();
	}
	
	public SimpleDataRetriever(String key)
	{
		this.key = key;
	}
	
	public Method chooseMethod(Method one, Method two) {
		if (one == null) {
			return two;
		} else if (two == null) {
			return one;
		} else if (one.getParameterTypes().length == 1
				&& one.getParameterTypes()[0].equals(String.class)) {
			return one;
		} else if (two.getParameterTypes().length == 1
				&& two.getParameterTypes()[0].equals(String.class)) {
			return two;
		} else if (one.getParameterTypes().length == 1
				&& !one.getParameterTypes()[0].isPrimitive()) {
			return one;
		} else if (two.getParameterTypes().length == 1
				&& !two.getParameterTypes()[0].isPrimitive()) {
			return two;
		} else {
			return one;
		}
	}
    
    /**
	 * Retrieves a value from a storage object - either a Map or a Data class
	 * object in this case.
	 * 
	 * @param store
	 * @param key
	 * @return
	 */
    protected Object getValueFromStore(Object store, Object key)
    {
       if (store instanceof ResultSet)
       {
           try
           {
               return ((ResultSet) store).getObject((String) key);
           } catch(SQLException e)
           {
               //failed to find a value - not necessarily a tragedy.
           }
       }
       else if (store instanceof Map)
       {
          return ((Map) store).get(key);
       }
       return null;
    }

    @CoinjemaDependency(alias="log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

}
