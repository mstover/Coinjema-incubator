package strategiclibrary.service.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.context.GroovyScriptEvaluator;
import org.coinjema.context.source.Resource;
import org.coinjema.util.Tuple;

import strategiclibrary.service.DataBase;
import strategiclibrary.service.sql.orm.ObjectMapSet;
import strategiclibrary.service.sql.orm.ObjectMapping;

@CoinjemaObject(type = "objectMappingService")
public class MappedEvaluator extends GroovyScriptEvaluator {

	DataBase db;

	ObjectMapSet mappings;

	public MappedEvaluator() {
	}

	@Override
	public Object evaluate(Resource arg0, Map arg1) {
		Tuple<String, String> sql = (Tuple<String, String>) super.evaluate(
				arg0, arg1);
		ObjectMapping props = (ObjectMapping) mappings.getMapping(sql.first);
		ResultSet rs = null;
		try {
			rs = db.executeQuery(sql.second);
			return props.getObject(rs);
		} catch (Exception e) {
			throw new RuntimeException("Problem mapping object", e);
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@CoinjemaDependency(type = "objectMappingService")
	public void setMappingService(DefaultMappingService service) {
		mappings = service.mappings;
	}

	@CoinjemaDependency(method = "database")
	public void setDatabase(DataBase db) {
		this.db = db;
	}

}
