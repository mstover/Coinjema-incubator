package strategiclibrary.service.sql.orm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import org.coinjema.util.Functor;

import strategiclibrary.util.Converter;

public class EnhancedDataRetriever extends SimpleDataRetriever {
	
	Functor<Object> function;
	Collection<Object> ignoreValues;
	String def;
	Class coerceTo;

	public EnhancedDataRetriever() {
		super();
	}

	public EnhancedDataRetriever(String key) {
		super(key);
	}

	public Class getCoerceTo() {
		return coerceTo;
	}

	public void setCoerceTo(Class coerceTo) {
		this.coerceTo = coerceTo;
	}

	public String getDef() {
		return def;
	}

	public void setDef(String def) {
		this.def = def;
	}

	public Functor<Object> getFunction() {
		return function;
	}

	public void setFunction(Functor<Object> function) {
		this.function = function;
	}

	public Collection<Object> getIgnoreValues() {
		return ignoreValues;
	}

	public void setIgnoreValues(Collection<Object> ignoreValues) {
		this.ignoreValues = ignoreValues;
	}
	
	@Override
	protected Object getValueFromStore(Object store, Object key) {
		Object retVal = null;
		if (store instanceof ResultSet) {
			try
            {
                retVal = ((ResultSet) store).getObject(getKey());
            } catch(SQLException e)
            {
                throw new RuntimeException(e);
            }
		} else if (store instanceof Map) {
			retVal = ((Map) store).get(getKey());
		}
		if(getIgnoreValues() != null && getIgnoreValues().contains(retVal))
		{
			retVal = null;
		}
		if(retVal == null)
		{
			retVal = getDef();
		}
		if(getFunction() != null)
		{
			if(getCoerceTo() != null)
			{
				retVal = Converter.convert(retVal,getCoerceTo());
			}
			retVal = getFunction().invoke(new Object[]{retVal});
		}
		return retVal;
	}

}
