import strategiclibrary.service.sql.orm.*;
import strategiclibrary.nontest.service.sql.mock.*;

SimpleObjectMapping map = new SimpleObjectMapping(objectClass:Contact,
								objectMapSet:mapSet);
		map.setMappings( ["setEmail" : new SimpleDataRetriever(key : "MANAGER_EMAIL"), 
			"setFirstName" : new SimpleDataRetriever(key : "MANAGER_FIRSTNAME"),
			"setLastName" : new SimpleDataRetriever(key : "MANAGER_LASTNAME")]);
return map;