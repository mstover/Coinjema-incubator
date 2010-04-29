package com.coinjema.client.event;

public class CommentEvent {

	public static final int REPLY_BUTTON = 0;

	public static final int highestEvent = 16;

	public int getType() {
		return type;
	}

	private final int type;
	private final Object source;

	public CommentEvent(int type, Object source) {
		this.type = type;
		this.source = source;
	}

	public Object getSource() {
		return source;
	}

}
