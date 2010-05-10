/*
 * Created on Oct 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.template.velocity;

import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.template.NoSuchTemplateException;
import strategiclibrary.service.template.TemplateService;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
@CoinjemaObject(type = "velocityService")
public class VelocityService implements TemplateService {
	public static final String COMPONENT = "components";

	public static final String NAME = "name";

	Properties velocityProperties;

	Map<String, Object> components;

	VelocityEngine velocity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.avalon.framework.activity.Initializable#initialize()
	 */
	public void initialize() {
		try {
			velocity = new VelocityEngine();
			velocity.init(velocityProperties);
		} catch (Exception e) {
			getLog().error("Couldn't initialize Velocity", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#mergeTemplate(java.lang.String,
	 *      org.apache.velocity.context.Context, java.io.StringWriter)
	 */
	public boolean mergeTemplate(String templateName, Map velocityContext,
			Writer results) {
		try {
			return velocity.mergeTemplate(templateName, (String) velocity
					.getProperty("input.encoding"),
					getContext(velocityContext), results);
		} catch (ResourceNotFoundException e) {
			getLog().error("problem merging velocity template", e);
			throw new NoSuchTemplateException(
					"problem merging velocity template", e);
		} catch (Exception e) {
			getLog().error("problem merging velocity template", e);
			throw new RuntimeException("problem merging velocity template", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#getContext()
	 */
	protected Context getContextBindings() {
		VelocityContext context = new VelocityContext();
		populateContext(context);
		return context;
	}

	private Context populateContext(Context context) {
		for (String name : components.keySet()) {
			context.put(name, components.get(name));
		}
		return context;
	}

	protected Context getContext(Map values) {
		Context context = getContextBindings();
		if (values == null) {
			return context;
		}
		Iterator iter = values.keySet().iterator();
		while (iter.hasNext()) {
			String item = iter.next().toString();
			context.put(item, values.get(item));
		}
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#evalutate(org.apache.velocity.context.Context,
	 *      java.io.Writer, java.lang.String, java.io.Reader)
	 */
	public boolean evaluate(Map context, Writer results, String logTag,
			Reader template) {
		try {
			return velocity.evaluate(getContext(context), results, logTag,
					template);
		} catch (Exception e) {
			getLog().error("problem merging velocity template", e);
			throw new RuntimeException("problem merging velocity template");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#evaluate(org.apache.velocity.context.Context,
	 *      java.io.Writer, java.lang.String, java.lang.String)
	 */
	public boolean evaluate(Map context, Writer results, String logTag,
			String in) {
		try {
			return velocity.evaluate(getContext(context), results, logTag, in);
		} catch (Exception e) {
			getLog().error("problem merging velocity template: " + in, e);
			throw new RuntimeException("problem merging velocity template");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#execute(java.lang.String,
	 *      java.util.Map)
	 */
	public Object execute(String scriptName, Map context) {
		throw new RuntimeException("This method inappropriate for"
				+ " Velocity templates", new UnsupportedOperationException());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see strategiclibrary.service.template.TemplateService#executeString(java.lang.String,
	 *      java.util.Map)
	 */
	public Object executeString(String script, Map context) {
		StringWriter results = new StringWriter();
		try {
			velocity.evaluate(getContext(context), results, "velocity", script);
			return results.toString();
		} catch (Exception e) {
			getLog().error("problem merging velocity template: " + script, e);
			throw new RuntimeException("problem merging velocity template");
		}
	}

	/**
	 * @param components
	 *            The components to set.
	 */
	@CoinjemaDependency(method = "components")
	public void setComponents(Map<String, Object> components) {
		this.components = components;
	}

	/**
	 * @param velocityProperties
	 *            The velocityProperties to set.
	 */
	@CoinjemaDependency(method = "conf", order = CoinjemaDependency.Order.LAST)
	public void setVelocityProperties(Properties velocityProperties) {
		this.velocityProperties = velocityProperties;
		initialize();
	}

	private Logger log;

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

	protected Logger getLog() {
		return log;
	}

	public boolean evaluate(Context context, Writer writer, String logTag,
			Reader reader) {
		try {
			return velocity.evaluate(populateContext(context), writer, logTag,
					reader);
		} catch (Exception e) {
			getLog().error("problem merging velocity template", e);
			throw new RuntimeException("problem merging velocity template");
		}
	}

	public boolean evaluate(Context context, Writer out, String logTag,
			String instring) {
		try {
			return velocity.evaluate(populateContext(context), out, logTag,
					instring);
		} catch (Exception e) {
			getLog().error("problem merging velocity template", e);
			throw new RuntimeException("problem merging velocity template");
		}
	}
}
