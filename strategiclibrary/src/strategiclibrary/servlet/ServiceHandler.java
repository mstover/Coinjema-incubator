package strategiclibrary.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaContextTrack;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;
import org.coinjema.util.Functor;

import strategiclibrary.service.template.velocity.VelocityService;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import strategiclibrary.service.webaction.WebRequestHandler;

@CoinjemaObject(type="webservice")
public class ServiceHandler {
    WebRequestHandler handler;
    VelocityService velocity;
    Logger log;

	public ServiceHandler() {
	}
	
	public ServiceHandler(CoinjemaContext c)
	{
		
	}
	
	public HandlerData handle(HttpServletRequest request, HttpServletResponse response,ServletContext app) throws Exception
	{
		ServletHandlerData info = new ServletHandlerData(request,response,request.getSession(true),app);
		handler.performAction(info);
		velocity.mergeTemplate(request.getServletPath(),info.getBeanMap(),response.getWriter());
		return info;
	}

	public HandlerData handle(String template,HttpServletRequest request, HttpServletResponse response,ServletContext app) throws Exception
	{
		ServletHandlerData info = new ServletHandlerData(request,response,request.getSession(true),app);
		handler.performAction(info);
		velocity.evaluate(info.getBeanMap(),response.getWriter(),"web-controller",template);
		return info;
	}
    
    @CoinjemaDependency(type="webRequestService")
    public void setWebRequestHandler(WebRequestHandler h)
    {
        this.handler = h;
    }
    
    @CoinjemaDependency(type="velocityService",method="velocityService")
    public void setVelocityService(VelocityService vs)
    {
    	velocity = vs;
    }
    

	public HandlerData performAction(HttpServletRequest request, HttpServletResponse response,ServletContext app) throws ActionException {
		HandlerData info = new ServletHandlerData(request,response,request.getSession(true),app);
		handler.performAction(info);
		return info;
	}

	@CoinjemaDependency(alias="log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

}
