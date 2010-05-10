package strategiclibrary.service;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import strategiclibrary.service.template.TemplateService;

public class BatchUpdate {
	
	DataBase database;
	TemplateService templates;
	
	List<String> queries = new LinkedList<String>();
	
	BatchUpdate(DataBase db,TemplateService ts)
	{
		this.database = db;
		templates = ts;
	}
	
	public void addUpdate(String queryName,Map values)
	{
		StringWriter results = new StringWriter();
		templates.mergeTemplate(queryName,database.getTemplateContext(values), results);
		queries.add(results.toString());
	}
	
	public void addUpdate(String query)
	{
		queries.add(query);
	}
	
	public int[] execute() throws Exception
	{
		int[] rets = database.executeBatch(queries);
		queries.clear();
		return rets;
		
	}

}
