/**
 * 
 */
package com.coinjema.client.event;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.comment.ui.CommentContainer;
import com.coinjema.client.comment.ui.CommentWidget;
import com.coinjema.client.comment.ui.StoryToolbar;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;

/**
 * @author mstover
 * 
 */
public class ReplyButtonListener implements CommentEventListener {

	TextArea replyText;
	CommentContainer cc;
	boolean inProgress = false;

	/**
	 * 
	 */
	public ReplyButtonListener() {
		cc = ClientCodeRegistry.commentContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.coinjema.client.event.CommentEventListener#commentActionPerformed(
	 * com.coinjema.client.event.CommentEvent)
	 */
	@Override
	public void commentActionPerformed(CommentEvent event) {
		if (inProgress) {
			return;
		} else {
			inProgress = true;
		}
		if (ClientCodeRegistry.system().loggedIn()) {

			final Object com = event.getSource();
			final FlowPanel p = new FlowPanel();
			p.addStyleName("reply-panel");
			replyText = new TextArea();
			replyText.addStyleName("reply-textarea");
			p.add(replyText);
			Button submitButton = new Button("Submit");
			submitButton.addStyleName("submit-reply-button");
			submitButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (com instanceof CommentWidget) {
						submitReplyBox((CommentWidget) com, p);
					} else {
						((StoryToolbar) com).remove(p);
						ClientCodeRegistry.system().submitComment(-1,
								replyText.getText(), null);
					}
					inProgress = false;
				}
			});
			Button cancelButton = new Button("Cancel");
			cancelButton.addStyleName("cancel-reply-button");
			cancelButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (com instanceof CommentWidget) {
						cc.remove(p);
					} else {
						((StoryToolbar) com).remove(p);
					}
					inProgress = false;
				}
			});
			FlowPanel buttons = new FlowPanel();
			buttons.addStyleName("buttons");
			buttons.add(submitButton);
			buttons.add(cancelButton);
			p.add(buttons);
			p.add(replyText);
			replyText.setTabIndex(10000);
			submitButton.setTabIndex(10001);
			cancelButton.setTabIndex(10002);
			if (com instanceof CommentWidget) {
				insertReplyBox((CommentWidget) com, p);
			} else {
				insertReplyBox((StoryToolbar) com, p);
			}
			replyText.setFocus(true);
		}

	}

	public void submitReplyBox(CommentWidget com, Panel replyBox) {
		cc.remove(replyBox);
		CommentWidget newComment = cc.createCommentBox(replyText
				.getText(), com);
		cc.insert(newComment, cc.findWidgetIndex(com) + 1);
		ClientCodeRegistry.system().submitComment(com.getId(),
				replyText.getText(), newComment);
	}

	public void insertReplyBox(StoryToolbar com, Panel replyBox) {
		com.add(replyBox);
		if (replyBox.getAbsoluteTop() + replyBox.getOffsetHeight() > Window
				.getClientHeight()) {
			ClientCodeRegistry.getMainCommentScroller()
					.setScrollPosition(
							ClientCodeRegistry.getMainCommentScroller()
									.getScrollPosition()
									+ replyBox.getOffsetHeight());
		}
	}

	public void insertReplyBox(CommentWidget com, Panel replyBox) {
		replyBox.addStyleName("comment_level_" + (com.getLevel() + 1));
		cc.insert(replyBox, cc.findWidgetIndex(com) + 1);
		if (replyBox.getAbsoluteTop() + replyBox.getOffsetHeight() > Window
				.getClientHeight()) {
			ClientCodeRegistry.getMainCommentScroller()
					.setScrollPosition(
							ClientCodeRegistry.getMainCommentScroller()
									.getScrollPosition()
									+ replyBox.getOffsetHeight());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.event.CommentEventListener#eventsOfInterest()
	 */
	@Override
	public int[] eventsOfInterest() {
		return new int[] { CommentEvent.REPLY_BUTTON };
	}

}
