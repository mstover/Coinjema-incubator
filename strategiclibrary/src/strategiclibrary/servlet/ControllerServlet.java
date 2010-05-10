/*
 * Created on Mar 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package strategiclibrary.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaObject;

/**
 * @author mike
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
@CoinjemaObject
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1;

	ServletContext servletContext;

	Map<String, ServiceHandler> contextHandlers = Collections
			.synchronizedMap(new HashMap<String, ServiceHandler>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			ServiceHandler handler = resolveContext(request);
			handler.handle(request, response, servletContext);
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		} finally {
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			ServiceHandler handler = resolveContext(request);
			StringBuffer postData = new StringBuffer();
			BufferedReader read = new BufferedReader(new InputStreamReader(request.getInputStream(),"utf-8"));
			String s = null;
			while((s=read.readLine()) != null)
				postData.append(s);
			read.close();
			handler.handle(postData.toString(),request, response, servletContext);
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		} finally {
			response.getWriter().flush();
			response.getWriter().close();
		}
	}
	


	protected ServiceHandler resolveContext(HttpServletRequest request) {
		String cntxt = request.getRequestURI();
		if(request.getSession().getAttribute("coinjemaContext") != null)
			cntxt = ((CoinjemaContext)request.getSession().getAttribute("coinjemaContext")).getName();
		else if(cntxt.startsWith("/")) cntxt = cntxt.substring(1);
		if (!contextHandlers.containsKey(cntxt)) {
			contextHandlers.put(cntxt, new ServiceHandler(
					new CoinjemaContext(cntxt)));
		}
		ServiceHandler handler = contextHandlers.get(cntxt);
		return handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		servletContext = config.getServletContext();
	}



}
