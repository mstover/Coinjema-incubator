package strategiclibrary.service.webaction.tags;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.coinjema.context.CoinjemaContext;
import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.beans.ErrorsBean;
import strategiclibrary.servlet.ServiceHandler;

public class HandleUserActions extends AbstractWebActionTag {
	private static final long serialVersionUID = 1;

	/**
	 * Calls into the WebRequestHandlerContainer service to handle all actions
	 * that exist in the servlet request. Also takes care of redirecting the
	 * request in the event of an error, and it creates a Logger object for the
	 * page to use - "log" in the PageContext.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		ServiceHandler handler = initHandler(getContext().getSession());
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

	private ServiceHandler initHandler(HttpSession session) {
			CoinjemaContext cntxt = (CoinjemaContext)session.getAttribute("coinjemaContext");
			if(cntxt != null)
			{
				ServiceHandler handler = new ServiceHandler(cntxt);
				return handler;
			}
			else
			{
				ServiceHandler handler = new ServiceHandler(defaultContext);
				return handler;
			}
	}
	
	@CoinjemaDependency(alias="defaultContext")
	public void setDefaultContext(String cntx)
	{
		defaultContext = new CoinjemaContext(cntx);
	}
	
	CoinjemaContext defaultContext;
}
