/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.coinjema.client.comments.Story;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;

/**
 * @author mstover
 * 
 */
public class TopCommentWidget extends CommentWidget {

	Anchor title;
	Label author;
	long id;

	/**
	 * 
	 */
	public TopCommentWidget() {
		// TODO Auto-generated constructor stub
	}

	public TopCommentWidget(Story child) {
		title = new Anchor(child.getTitle(), "?story=" + child.getId());
		author = new Label(child.getAuthor(), false);
		this.id = child.getId();
		initialize();
	}

	private void initialize() {
		addStyleName("story");
		title.addStyleName("title");
		Label submittedLabel = new Label("Submitted by: ");
		submittedLabel.addStyleName("submitted-by");
		author.addStyleName("author");
		add(title);
		add(submittedLabel);
		add(author);
	}

	@Override
	public void collapseComment() {
		addStyleName("collapsed");

	}

	@Override
	public void collapseCommentText() {
		addStyleName("collapsed");

	}

	@Override
	public void expandComment() {
		removeStyleName("collapsed");

	}

	@Override
	public void expandCommentText() {
		removeStyleName("collapsed");

	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public long getParentId() {
		return -1L;
	}

	@Override
	public int getLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setId(long uniqueId) {
		id = (Long) uniqueId;
		// TODO Auto-generated method stub

	}
}
