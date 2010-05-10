package strategiclibrary.service.sql.orm;

import java.lang.reflect.Method;

public interface DataRetriever {
	
	public Object[] retrieveData(Object dataStore,String methodName,Method meth); 
	
	public Method chooseMethod(Method one,Method two);

}
