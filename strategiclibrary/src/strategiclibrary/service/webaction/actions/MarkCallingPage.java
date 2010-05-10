package strategiclibrary.service.webaction.actions;

import java.util.LinkedList;

import strategiclibrary.service.webaction.AbstractWebAction;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

public class MarkCallingPage extends AbstractWebAction {

	public void performAction(HandlerData actionInfo) throws ActionException {
		LinkedList<String> history = actionInfo.getUserBean("requestHistory",LinkedList.class);
		if(history.size() >= 2)
		{
			if(actionInfo.hasParam("mark_name"))
			{
				actionInfo.setUserBean("callingUrl_"+actionInfo.getParameter("mark_name"), history.get(1));
			}
			else actionInfo.setUserBean("callingUrl", history.get(1));
		}
	}

	public String getName() {
		return "mark_calling_page";
	}

}
