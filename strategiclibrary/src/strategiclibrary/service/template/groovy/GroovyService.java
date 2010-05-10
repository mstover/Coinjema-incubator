/*
 * Created on Aug 16, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.service.template.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.text.Template;
import groovy.util.GroovyScriptEngine;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.template.TemplateService;

/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
@CoinjemaObject(type="groovyService")
public class GroovyService implements TemplateService {
    public static final String COMPONENT = "components";

    public static final String NAME = "name";

    GroovyScriptEngine engine;

    GroovyTemplateEngine tEngine;

    String[] resourcePathRoots;

    Map<String, Object> components;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws IOException {
        engine = new GroovyScriptEngine(resourcePathRoots[0]);
        tEngine = new GroovyTemplateEngine(resourcePathRoots);
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.template.TemplateService#mergeTemplate(java.lang.String,
     *      java.util.Map, java.io.Writer)
     */
    public boolean mergeTemplate(String templateName, Map dataContext,
            Writer results) {
        try {
            Template t = tEngine.getTemplate(templateName);
            t.make(dataContext).writeTo(results);
            return true;
        } catch (Exception e) {
            getLog().error("Couldn't evaluate template " + templateName, e);
            throw new RuntimeException("Couldn't evaluate template " + templateName, e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.template.TemplateService#evaluate(java.util.Map,
     *      java.io.Writer, java.lang.String, java.io.Reader)
     */
    public boolean evaluate(Map context, Writer results, String logTag,
            Reader template) {
        try {
            Template t = tEngine.getTemplate(template);
            t.make(context).writeTo(results);
            return true;
        } catch (Exception e) {
            getLog().error("Couldn't evaluate template ", e);
            throw new RuntimeException("Couldn't evaluate template ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.template.TemplateService#evaluate(java.util.Map,
     *      java.io.Writer, java.lang.String, java.lang.String)
     */
    public boolean evaluate(Map context, Writer results, String logTag,
            String in) {
        try {
            Template t = tEngine.getTemplate(new StringReader(in));
            t.make(context).writeTo(results);
            return true;
        } catch (Exception e) {
            getLog().error("Couldn't evaluate template ", e);
            throw new RuntimeException("Couldn't evaluate template ", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.template.TemplateService#execute(java.lang.String,
     *      java.util.Map)
     */
    public Object execute(String scriptName, Map context) {
        try {
            return engine.run(scriptName, getContext(context));
        } catch (Exception e) {
            getLog().error("Couldn't run script " + scriptName, e);
            throw new RuntimeException("Couldn't run script " + scriptName, e);
        }
    }

    @CoinjemaDependency(method = "components")
    public void setComponents(Map<String, Object> components) {
        this.components = components;
    }

    @CoinjemaDependency(method = "resourcePaths", order = CoinjemaDependency.Order.LAST)
    public void setResourcePaths(String[] paths) {
        this.resourcePathRoots = paths;
        try {
            initialize();
        } catch (IOException e) {
            throw new RuntimeException(
                    "Resource paths for GroovyService were probably bad", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see strategiclibrary.service.template.TemplateService#executeString(java.lang.String,
     *      java.util.Map)
     */
    public Object executeString(String script, Map context) {
        try {
            GroovyShell s = new GroovyShell(getContext(context));
            return s.evaluate(script);
        } catch (Exception e) {
            getLog().error("Couldn't run arbitryar script ", e);
            throw new RuntimeException("Couldn't run script arbitary script", e);
        }
    }

    protected Binding getContext() {
        Binding context = new Binding();
        for (String name : components.keySet()) {
            context.setVariable(name, components.get(name));
        }
        context.setVariable("log", getLog());
        return context;
    }

    protected Binding getContext(Map values) {
        Binding context = getContext();
        if (values == null) {
            return context;
        }
        Iterator iter = values.keySet().iterator();
        while (iter.hasNext()) {
            String item = iter.next().toString();
            context.setVariable(item, values.get(item));
        }
        return context;
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