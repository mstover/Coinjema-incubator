package com.coinjema.client.event;

import java.util.ArrayList;
import java.util.List;

import com.coinjema.client.ClientCodeRegistry;

public class EventCatcher {

	List[] listeners = new ArrayList[CommentEvent.highestEvent];

	public EventCatcher() {
		ClientCodeRegistry.eventCatcher(this);
	}

	public void addListener(CommentEventListener lis) {
		for (int eventType : lis.eventsOfInterest()) {
			if (listeners[eventType] == null) {
				listeners[eventType] = new ArrayList();
			}
			listeners[eventType].add(lis);
		}
	}

	public void postEvent(CommentEvent e) {
		for (Object listener : listeners[e.getType()]) {
			((CommentEventListener) listener).commentActionPerformed(e);
		}
	}

}
