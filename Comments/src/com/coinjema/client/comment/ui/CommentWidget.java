/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author mstover
 * 
 */
public abstract class CommentWidget extends FlowPanel {

	abstract public int getLevel();

	abstract public void collapseComment();

	abstract public long getId();
	
	abstract public long getParentId();

	abstract public void expandComment();

	abstract public void collapseCommentText();

	abstract public void expandCommentText();

	abstract public void setId(long uniqueId);
	
	
	
}
