import strategiclibrary.service.sql.orm.*;
import strategiclibrary.nontest.service.sql.mock.*;

AbstractObjectMapping map = new SimpleObjectMapping(objectClass:Contact,
								objectMapSet:mapSet);
map.setMappings(["setEmail" : new EnhancedDataRetriever(key : "CONTACT_EMAIL", 
										def : "kens@lazerinc.com"),
			"setFirstName" : new SimpleDataRetriever(key : "CONTACT_FIRSTNAME"),
			"setLastName" : new EnhancedDataRetriever(key : "CONTACT_LASTNAME",
							 def : "Stover",
							 ignoreValues : ["","badValue"] ),
			"setManager" : new SimpleObjectMapping(name:"ManagerMapper",methodName:"setManager",objectMapSet:mapSet),
			"setTodo" : new SimpleObjectMapping(methodName:"setTodo",
								objectClass:Todo,objectMapSet:mapSet,
								mappings:["setDescription" : new SimpleDataRetriever(key : "TODO_DESCRIPTION")])
				]);
								
return map;
		