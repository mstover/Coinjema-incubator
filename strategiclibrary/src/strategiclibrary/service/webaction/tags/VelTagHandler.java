package strategiclibrary.service.webaction.tags;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.apache.velocity.context.Context;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaContextTrack;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.template.velocity.VelocityService;

@CoinjemaObject(type="veltag")
public class VelTagHandler {
	
	VelocityService service;

	public VelTagHandler() {
	}
	
	public VelTagHandler(CoinjemaContext cc)
	{
		
	}
	
	@CoinjemaDependency(type="velocityService",method="template")
	public void setVelocityService(VelocityService s)
	{
		service = s;
	}
	
	public VelocityService getService()
	{
		return service;
	}

	public boolean evaluate(Context context, Writer writer, String logTag, Reader reader) {
		return service.evaluate(context, writer, logTag, reader);
	}

	public boolean evaluate(Context context, Writer out, String logTag, String instring) {
		return service.evaluate(context, out, logTag, instring);
	}

	public boolean evaluate(Map context, Writer results, String logTag, Reader template) {
		return service.evaluate(context, results, logTag, template);
	}

	public boolean evaluate(Map context, Writer results, String logTag, String in) {
		return service.evaluate(context, results, logTag, in);
	}

	public Object execute(String scriptName, Map context) {
		return service.execute(scriptName, context);
	}

}
