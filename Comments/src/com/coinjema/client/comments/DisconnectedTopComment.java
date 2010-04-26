package com.coinjema.client.comments;

public class DisconnectedTopComment extends Story {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DisconnectedTopComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DisconnectedTopComment(Story c) {
		super(c.getAuthor(), c.getTitle(), c.getText(), c
				.getLinks());
		setId(c.getId());
	}
}
