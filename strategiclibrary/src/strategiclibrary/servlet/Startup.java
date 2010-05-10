/*
 * Created on Aug 12, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package strategiclibrary.servlet;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.coinjema.context.ContextFactory;
import org.coinjema.context.source.FileContextSource;

/**
 * @author mstover
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class Startup implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		ContextFactory.destroyContext(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext context = arg0.getServletContext();
		setupRootContext(context);
		setupAdditionalContexts(context);
	}

	private void setupAdditionalContexts(ServletContext context) {
		Enumeration enumer = context.getInitParameterNames();
		while(enumer.hasMoreElements())
		{
			String name = (String)enumer.nextElement();
			if(name.startsWith("coinjema_context"))
			{
				String contextName = context.getInitParameter(name);
				String path = context.getInitParameter(name + "_path");
				try {
					if(path != null && path.length() > 0)
					{
						ContextFactory.createContext(contextName,
								new FileContextSource(new File(context.getRealPath(""),path)));
					}
					else
					{
						ContextFactory.createContext(contextName,new FileContextSource(new File(context.getRealPath(""))));
					}
							
				} catch (Exception e) {
					System.out.println("name = " + name + " rootpath = " + context.getRealPath("") + " path = " + context.getInitParameter(name+"_path"));
					e.printStackTrace();
				}
			}
		}
	}

	private void setupRootContext(ServletContext context) {
		String coinjemaRootDir = context.getInitParameter("coinjema_root");
		if (coinjemaRootDir != null && coinjemaRootDir.length() > 0) {
			try {
				ContextFactory.createRootContext(new FileContextSource(new File(context.getRealPath(""),
						coinjemaRootDir)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Configuring logging system based on property file " + (context.getRealPath("")+"/"+coinjemaRootDir+"/logging4j.properties"));
		PropertyConfigurator.configure(context.getRealPath("")+"/"+coinjemaRootDir+"/logging4j.properties");
	}
}
