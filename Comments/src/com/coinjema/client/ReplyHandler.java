package com.coinjema.client;

import com.coinjema.client.comment.ui.CommentContainer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TextArea;

public class ReplyHandler implements ClickHandler {

	CommentContainer comments;
	ClientCommentSystem system;

	TextArea replyText;

	public ReplyHandler(CommentContainer cc, ClientCommentSystem system) {
		comments = cc;
		this.system = system;
	}

	@Override
	public void onClick(ClickEvent event) {

	}

	public String getReplyText() {
		return replyText.getText();
	}
}
