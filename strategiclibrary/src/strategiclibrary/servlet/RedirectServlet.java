package strategiclibrary.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.beans.ErrorsBean;

public class RedirectServlet extends ControllerServlet {
	Logger log;

	public Logger getLog() {
		return log;
	}

	@CoinjemaDependency(alias="log4j")
	public void setLog(Logger log) {
		this.log = log;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}

	private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			ServiceHandler handler = resolveContext(request);
			request.setAttribute("redirecting", "true");
			HandlerData info = handler.performAction(request,response, servletContext);
			info.setUserBean("redirecting",true);
			ErrorsBean bean = (ErrorsBean) info.getBean(ErrorsBean.NAME);
			if (bean != null && bean.isRedirect()) {
				getLog().debug("Redirecting to: " + bean.getRedirectPage());
				try {
					response.sendRedirect(bean.getRedirectPage());
				} catch (IOException e) {
					getLog().error("Problem redirecting", e);
				}
			}
			response.sendRedirect(info.getParameter("redirectUrl",""));
		} catch (Exception e) {
			e.printStackTrace(response.getWriter());
		} finally {
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		handleRequest(request, response);
	}
}
