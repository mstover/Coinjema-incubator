package strategiclibrary.service.webaction.tags;

import javax.servlet.jsp.JspException;

public class SetRequestParameter extends AbstractWebActionTag {

	private String param;

	private String value;

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Adds a parameter to the request parameters.
	 * 
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspException {
		if (value == null && bodyContent != null) {
			value = bodyContent.getString();
		}
		getContext().getRequest().setAttribute(param, value);
		return super.doEndTag();
	}

}
