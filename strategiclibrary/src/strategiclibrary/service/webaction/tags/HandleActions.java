/*
 * Created on Sep 9, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package strategiclibrary.service.webaction.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.coinjema.context.CoinjemaContext;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;
import strategiclibrary.service.webaction.WebRequestHandler;
import strategiclibrary.service.webaction.beans.ErrorsBean;
import strategiclibrary.servlet.ServiceHandler;

/**
 * @author Michael Stover
 * @version $Revision: 1.1 $
 */
public class HandleActions extends AbstractWebActionTag {
	private static final long serialVersionUID = 1;

	ServiceHandler handler;

	String context = null;

	/**
	 * Calls into the WebRequestHandlerContainer service to handle all actions
	 * that exist in the servlet request. Also takes care of redirecting the
	 * request in the event of an error, and it creates a Logger object for the
	 * page to use - "log" in the PageContext.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		initHandler();
		try {
			HandlerData actionInfo = handler.performAction(
					(HttpServletRequest) getContext().getRequest(),
					(HttpServletResponse) getContext().getResponse(),
					getContext().getServletContext());
			ErrorsBean bean = (ErrorsBean) actionInfo.getBean(ErrorsBean.NAME);
			if (bean != null && bean.isRedirect()) {
				getLogger().debug("Redirecting to: " + bean.getRedirectPage());
				try {
					((HttpServletResponse) getContext().getResponse())
							.sendRedirect(bean.getRedirectPage());
					return SKIP_PAGE;
				} catch (IOException e) {
					getLogger().error("Problem redirecting", e);
				}
			}
		} catch (ActionException e) {
			getLogger()
					.error(
							"An action Exception performing actions on WebRequestHandler",
							e);
		} catch (Exception e) {
			getLogger()
					.error(
							"A Service Exception performing actions on WebRequestHandler",
							e);
		}
		return super.doEndTag();
	}

	private void initHandler() {
		if (handler == null) {
			String cntxt = (context == null) ? ((HttpServletRequest) getContext()
					.getRequest()).getRequestURI()
					: context;
			if (cntxt.startsWith("/"))
				cntxt = cntxt.substring(1);
			getLogger().debug("Context path = " + cntxt);
			this.getLogger().debug("Creating handler for context: " + cntxt);
			handler = new ServiceHandler(new CoinjemaContext(cntxt));
		}
	}

	public void setContext(String context) {
		this.context = context;
	}

}
