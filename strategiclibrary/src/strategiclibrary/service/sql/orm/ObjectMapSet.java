/*
 * Created on Mar 8, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.sql.orm;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.collections.ConfigurationTree;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.classfinder.ClassFinderService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.util.TextFile;

/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
@CoinjemaObject(type="ormConfig")
public class ObjectMapSet {
	
	private static final String MAP_EXT = ".map";
	private static final String GROOVY_EXT = ".groovy";

	File mappingsDirectory;
    
    ClassFinderService classFinder;
    
    TemplateService groovyService;

	Map mappings;

	Collection packages;

	/**
	 * @return Returns the packages.
	 */
	public Collection getPackages() {
		return packages;
	}
    
    @CoinjemaDependency(type="classfinder")
    public void setClassFinder(ClassFinderService cf)
    {
        this.classFinder = cf;
    }
    
    @CoinjemaDependency(method="scriptService")
    public void setObjectMappingTemplateService(TemplateService ts)
    {
        groovyService = ts;
    }

	/**
	 * Tells the Map Set to begin constructing all the .map files it can find
	 * into a workable object mapping system.
	 *  
	 */
	public void initMappings() {
		File[] mapFiles = getMapFiles();

		for (int i = 0; i < mapFiles.length; i++) {
			try {
				if (getMapping(mapFiles[i].getName()) == null) {
					ConfigurationTree config = new ConfigurationTree(new FileReader(mapFiles[i]));
					String filename = config.getProperty("inherits", null);
					if (filename != null && getMapping(filename) == null) {
						ObjectMapping map = new XmlBasedObjectMapping(
								new ConfigurationTree(new FileReader(new File(mapFiles[i].getParent(), filename))),
                        this);
						map.setName(filename);
						addMapping(map);
					}
					ObjectMapping map = new XmlBasedObjectMapping(config, this);
					map.setName(mapFiles[i].getName());
					addMapping(map);
				}
			} catch (Exception e) {
                getLog().error("Couldn't load mapping file, ", e);
			}
		}
		mapFiles = getGroovyMapFiles();
		Map groovyValues = new HashMap();
		groovyValues.put("mapSet",this);
		for (int i = 0; i < mapFiles.length; i++) {
			getLog().info("getting mapping from file: " + mapFiles[i].getName());
			try {
					ObjectMapping map = (ObjectMapping)groovyService.executeString(getFileText(mapFiles[i]),groovyValues);
					map.setName(mapFiles[i].getName().substring(0,mapFiles[i].getName().lastIndexOf(".")));
					addMapping(map);
			} catch (Exception e) {
                getLog().error("Couldn't load mapping file, ", e);
			}
		}
	}
	
	private String getFileText(File f)
	{
		TextFile tf = new TextFile(f);
		return tf.getText();
	}
	
	protected void grabMappingImplementations()
	{
		try {
			List classes = classFinder.findClassesThatExtend(new Class[]{ObjectMapping.class});
			Iterator iter = classes.iterator();
			while(iter.hasNext())
			{
				Class clazz = (Class)iter.next();
				if(!clazz.equals(XmlBasedObjectMapping.class))
				{
					ObjectMapping mapping = (ObjectMapping)clazz.newInstance();
					mapping.setObjectMapSet(this);
					mapping.setName(clazz.getName());
					addMapping(mapping);
				}
			}
		} catch (Exception e) {
            getLog().warn("Problem finding object mappings",e);
		}
	}

	/**
	 * @return
	 */
	protected File[] getMapFiles() {
		File[] mapFiles = mappingsDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File f, String filename) {
				if (filename.endsWith(MAP_EXT)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return mapFiles;
	}
	
	protected File[] getGroovyMapFiles() {
		File[] mapFiles = mappingsDirectory.listFiles(new FilenameFilter() {
			public boolean accept(File f, String filename) {
				if (filename.endsWith(GROOVY_EXT)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return mapFiles;
	}

	/**
	 * @param packages
	 *            The packages to set.
	 */
	public void setPackages(Collection packages) {
		this.packages = packages;
	}

	public ObjectMapSet(String dir) {
		mappingsDirectory = new File(dir);
	}

	public void addMapping(ObjectMapping mapping) {
		if (mappings == null) {
			mappings = new HashMap();
		}
		mappings.put(mapping.getName(), mapping);
	}

	public void addMapping(String key, ObjectMapping mapping) {
		if (mappings == null) {
			mappings = new HashMap();
		}
		mappings.put(key, mapping);
	}

	public ObjectMapping getMapping(String mappingName) {
		if (mappings != null) {
			return (ObjectMapping) mappings.get(mappingName);
		} else
			return null;
	}

	/**
	 * @return Returns the defaultMapping.
	 */
	public ObjectMapping getDefaultMapping(String[] headers) {
		ObjectMapping defaultMapping = new XmlBasedObjectMapping();
		defaultMapping.setObjectClass(HashMap.class);
		for (int i = 0; i < headers.length; i++) {
			defaultMapping.put(headers[i], new SimpleDataRetriever(headers[i]));
		}
		return defaultMapping;
	}
       
       private Logger log;

       @CoinjemaDependency(alias="log4j")
       public void setLog(Logger l)
       {
           log = l;
       }
       
       protected Logger getLog()
       {
           return log;
       }
}
