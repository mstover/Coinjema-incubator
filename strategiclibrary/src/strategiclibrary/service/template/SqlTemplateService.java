/*
 * Created on Nov 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.service.template;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

/**
 * @author mike
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface SqlTemplateService {
	
	Statement getStatement(String queryName,Map values,Connection conn);

}
