/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.ClientCommentSystem;
import com.coinjema.client.event.CommentEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author mstover
 * 
 */
public class StoryToolbar extends FlowPanel {

	NewStoryBox dialog;
	ClientCommentSystem system;

	/**
	 * 
	 */
	public StoryToolbar(ClientCommentSystem system) {
		this.system = system;
		initialize();
	}

	private void initialize() {
		addStyleName("story-toolbar");
		if (system.loggedIn()) {
			if (Window.Location.getParameter("story") != null
					|| Window.Location.getParameter("stream") != null) {
				popupReplyBox();
			} else {
				popupNewStoryBox();
			}
		}
	}

	private void popupNewStoryBox() {
		Button submitStory = new Button("Submit Story",
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						dialog = new NewStoryBox(storySubmissionHandler(),
								closeHandler());
						add(dialog);
						dialog.setFocus(true);
					}
				});
		submitStory.addStyleName("submit-story-button");
		add(submitStory);
	}

	private ClickHandler storySubmissionHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				system.submitStory(dialog.getStoryTitle(), dialog
						.getStoryText(), dialog.getLinks());
			}
		};
	}

	private ClickHandler closeHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove(dialog);
			}
		};
	}

	private void popupReplyBox() {
		final StoryToolbar tb = this;
		Button replyButton = new Button("Reply", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientCodeRegistry.eventCatcher().postEvent(
						new CommentEvent(CommentEvent.REPLY_BUTTON, tb));
			}
		});
		replyButton.addStyleName("reply-button");
		add(replyButton);
	}

	public void update() {
		if (!system.loggedIn()) {
			clear();
		} else {
			if (getWidgetCount() == 0) {
				initialize();
			}
		}
	}

}
