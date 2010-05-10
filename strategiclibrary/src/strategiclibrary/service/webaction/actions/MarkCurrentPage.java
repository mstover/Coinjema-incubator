package strategiclibrary.service.webaction.actions;

import strategiclibrary.service.webaction.AbstractWebAction;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

public class MarkCurrentPage extends AbstractWebAction {

	public void performAction(HandlerData info) throws ActionException {
		info.setUserBean("markedUrl", info.getRequestMessage());
	}

	public String getName() {
		return "mark_page";
	}
	
	

}
