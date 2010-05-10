package strategiclibrary.service.webaction.actions;

import java.util.LinkedList;

import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.webaction.AbstractWebAction;
import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

@CoinjemaObject(type="requestHistory")
public class KeepHistory extends AbstractWebAction {
	int size = 5;

	public void performAction(HandlerData actionInfo) throws ActionException {
		LinkedList<String> history = actionInfo.getUserBean("requestHistory",LinkedList.class);
		if(history.size() == size)
		{
			history.removeLast();
		}
		history.addFirst(actionInfo.getRequestMessage());
	}

	public String getName() {
		return "keep_history";
	}

	@CoinjemaDependency(method="size",hasDefault=true)
	public void setSize(int size) {
		this.size = size;
	}

}
