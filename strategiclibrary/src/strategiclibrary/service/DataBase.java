/*
 * Created on Sep 10, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service;

import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.sql.SqlConverter;
import strategiclibrary.service.template.TemplateService;

/**
 * A base class that provides a convenient method for get database connection
 * objects.
 * 
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type = "database")
public class DataBase {
	protected static SqlConverter sqlConverter = new SqlConverter();

	DataSource dataSource;

	TemplateService sqlTemplates;

	boolean blockForConnection = false;

	public DataBase() {
	}

	public DataBase(CoinjemaContext context) {
	}

	protected Connection getConnection() throws SQLException {
		getLog().debug("Getting a connection");
		Connection c = null;
		doAgain: {
			try {
				c = dataSource.getConnection();
				assert (c != null);
				getLog().debug("got connection " + c);
				return c;
			} catch (Exception e) {
				if (!blockForConnection)
					if (e instanceof SQLException)
						throw (SQLException) e;
				else {
					getLog().warn("Failure to get datasource connection", e);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException er) {
						Thread.currentThread().interrupt();
					}
					break doAgain;
				}
			}
		}
		return c;
	}

	@CoinjemaDependency(type = "dataSource")
	public void setDataSource(DataSource ds) {
		dataSource = ds;
	}

	@CoinjemaDependency(method="blockForConnection",hasDefault=true)
	public void setBlockForConnection(boolean blockForConnection) {
		this.blockForConnection = blockForConnection;
	}

	@CoinjemaDependency(method = "sqlTemplateService")
	public void setTemplateService(TemplateService ts) {
		sqlTemplates = ts;
	}

	ResultSet getDataFromOracleDB(Connection conn, CallableStatement cstmt,
			int outParameterIndex, long time) throws Exception {
		ResultSet rs = null;
		try {
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(outParameterIndex);
			getLog().debug(
					" Just the query time: "
							+ (System.currentTimeMillis() - time) + " ms ");
			if (getLog().isDebugEnabled()) {
				getLog().debug(
						"query-to-Data time: "
								+ (System.currentTimeMillis() - time) + "ms");
			}
			return new ResultSetWrap(rs, cstmt, conn, getLog());
		} catch (Exception e) {
			ResultSetWrap.releaseConnectionResources(rs, cstmt, conn, getLog());
			throw e;
		}
	} // end of function

	protected long getStartTime() {
		long time = System.currentTimeMillis();
		getLog().debug("executing query: in ");
		if (getLog().isDebugEnabled()) {
			getLog().debug("executing query: ");
		}
		return time;
	}

	/**
	 * Execute a query and return the resulting Data object
	 * 
	 * @author Michael Stover
	 * @version $Revision: 1.1 $
	 */
	public ResultSet executeQuery(String query) throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			return executeQuery(query, st, conn);
		} catch (Exception e) {
			ResultSetWrap.releaseConnectionResources(null, st, conn, getLog());
			throw e;
		}
	}

	public int[] executeBatch(Collection<String> queries) throws Exception {
		Connection conn = null;
		Statement st = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			for (String sql : queries) {
				if (log.isDebugEnabled())
					log.debug("Executing batch statement: " + sql);
				st.addBatch(sql);
			}
			return st.executeBatch();
		} finally {
			ResultSetWrap.releaseConnectionResources(null, st, conn, getLog());
		}
	}

	public BatchUpdate createBatch() {
		return new BatchUpdate(this, sqlTemplates);
	}

	/**
	 * @param query
	 * @param st
	 * @return
	 * @throws SQLException
	 */
	ResultSet executeQuery(String query, Statement st, Connection conn)
			throws Exception {
		ResultSet rs = null;
		try {
			long time = System.currentTimeMillis();
			if (getLog().isDebugEnabled()) {
				getLog().debug("executing query: " + query);
			}
			rs = st.executeQuery(query);
			getLog().debug(
					"Just the query time: "
							+ (System.currentTimeMillis() - time) + "ms");
			if (getLog().isDebugEnabled()) {
				getLog().debug(
						"query-to-Data time: "
								+ (System.currentTimeMillis() - time) + "ms");
			}
			return new ResultSetWrap(rs, st, conn, getLog());
		} catch (Exception e) {
			getLog().warn("Error executing query: " + query);
			ResultSetWrap.releaseConnectionResources(rs, st, conn, getLog());
			throw e;
		}
	}

	/**
	 * Get a context object from the templating service.
	 * 
	 * @return
	 * @throws ServiceException
	 *             Context
	 */
	public Map getTemplateContext(Map values) {
		if (values == null) {
			values = new HashMap();
		}
		values.put("sql", sqlConverter);
		return values;
	}

	/**
	 * Execute a query template with the specific values given in the context.
	 * 
	 * @param queryName
	 * @param values
	 * @return
	 * @throws ServiceException
	 *             Data
	 */
	public ResultSet executeTemplateQuery(String queryName, Map values)
			throws Exception {
		long time = System.currentTimeMillis();
		StringWriter results = new StringWriter();
		sqlTemplates.mergeTemplate(queryName, values, results);
		ResultSet d = executeQuery(results.toString());
		getLog().debug(
				"Template query time for" + queryName + ": "
						+ (System.currentTimeMillis() - time) + "ms");
		return d;
	}

	public int executeTemplateUpdate(String queryName, Map values)
			throws Exception {
		StringWriter results = new StringWriter();
		sqlTemplates.mergeTemplate(queryName, values, results);
		return executeUpdate(results.toString());
	}

	/**
	 * Execute an update on the database.
	 * 
	 * @author Michael Stover
	 * @version $Revision: 1.1 $
	 */
	public int executeUpdate(String query) throws Exception {
		if (getLog().isDebugEnabled()) {
			getLog().debug("executing update: " + query);
		}
		Connection conn = null;
		Statement st = null;
		try {
			conn = getConnection();
			st = conn.createStatement();
			return st.executeUpdate(query);
		} catch (Exception e) {
			getLog().warn("Error executing update: " + query);
			throw e;
		} finally {
			ResultSetWrap.releaseConnectionResources(null, st, conn, getLog());
		}
	}

	private Logger log;

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

	protected Logger getLog() {
		return log;
	}

}
