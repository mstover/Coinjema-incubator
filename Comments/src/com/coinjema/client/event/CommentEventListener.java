package com.coinjema.client.event;

public interface CommentEventListener {

	public void commentActionPerformed(CommentEvent event);

	public int[] eventsOfInterest();

}
