/**
 * 
 */
package com.coinjema.client.comment.ui;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.event.CommentEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * @author mstover
 * 
 */
public class UserCommentWidget extends CommentWidget {

	private long id;

	final private int level;
	final private Label author;
	final private Label text;
	private Label debugLabel;
	private Label timeLabel;
	final private ToggleButton collapse;
	private CustomButton replyButton;
	private PushButton centerizeButton;
	private Button cycleButton;
	private Button hideButton;
	private FlowPanel toolbar;
	private FlowPanel bottombar;
	private long parentId;
	private long creationTime;

	public boolean isInView() {
		return inView;
	}

	public void setInView(boolean inView) {
		this.inView = inView;
		if (inView) {
			addStyleName("in-view");
		} else {
			removeStyleName("in-view");
		}
	}

	public boolean isNewComment() {
		return newComment;
	}

	public void setNewComment(boolean newComment) {
		this.newComment = newComment;
		if (newComment) {
			addStyleName("new_comment");
		} else {
			removeStyleName("new_comment");
		}
	}

	private boolean inView;
	private boolean newComment;

	/**
	 * 
	 */
	public UserCommentWidget(String text, String author, long id,
			long parentId, int level, long creationTime) {
		super();
		this.text = new Label(text, true);
		this.parentId = parentId;
		this.id = id;
		this.level = level;
		this.author = new Label(author);
		this.author.addStyleName("commentAuthor");
		centerizeButton = new PushButton(new Image(
				"images/buttons/center_n.png"), new Image(
				"images/buttons/center_p.png"));
		centerizeButton.setTitle("Center View On Comment");
		cycleButton = new Button("cycle");
		hideButton = new Button("hide");
		this.addStyleName("comment_level_" + level);
		addStyleName("comment");
		this.text.addStyleName("commentText");
		collapse = new ToggleButton("-", "+");
		this.creationTime = creationTime;
		populateToolbar();
		add(collapse);
		add(toolbar);
		add(this.text);
		add(bottombar);
	}

	@Override
	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	private void populateToolbar() {
		toolbar = new FlowPanel();
		collapse.setStyleName("collapse-icon");
		toolbar.add(this.author);
		timeLabel = new Label(calcTimeString());
		timeLabel.addStyleName("time-label");
		toolbar.add(timeLabel);
		toolbar.add(centerizeButton);
		toolbar.add(cycleButton);
		toolbar.add(hideButton);
		debugLabel = new Label("id=" + id + " parentid= " + parentId
				+ " level=" + level);
		toolbar.add(debugLabel);
		replyButton = new PushButton(new Image(
				"/images/buttons/reply_n.png"), new Image(
				"/images/buttons/reply_p.png"));
		replyButton.addStyleName("reply-button");
		replyButton.setTitle("Reply");
		toolbar.addStyleName("comment-toolbar");

		bottombar = new FlowPanel();
		bottombar.addStyleName("bottom-bar");
		bottombar.add(replyButton);
		final UserCommentWidget thisWidget = this;
		replyButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ClientCodeRegistry.eventCatcher().postEvent(
						new CommentEvent(CommentEvent.REPLY_BUTTON,
								thisWidget));
			}
		});
	}

	private String calcTimeString() {
		int time = (int) (System.currentTimeMillis() - creationTime) / 1000 / 60;
		if (time < 120) {
			// minutes
			if (time > 0) {
				return numericValue(time, "minute");
			} else {
				return "posted seconds ago";
			}
		} else {
			time = time / 60;
			if (time < 48) {
				// hours
				return numericValue(time, "hour");
			} else {
				time = time / 24;
				if (time < 60) {
					// days
					return numericValue(time, "day");
				} else {
					time = time / 30;
					if (time < 12) {
						// months
						return numericValue(time, "month");
					} else {
						time = time / 12;
						// years
						return numericValue(time, "year");
					}
				}
			}
		}
	}

	private String numericValue(int time, String string) {
		return "posted " + time + " " + string
				+ (time == 1 ? " ago" : "s ago");
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(long id) {
		this.id = id;
		debugLabel.setText("id=" + id + " parentid= " + parentId
				+ " level=" + level);
	}

	@Override
	public int getLevel() {
		return level;
	}

	public void addCollapseComment(ClickHandler command) {
		collapse.addClickHandler(command);
	}

	public void addCenterizeHandler(ClickHandler command) {
		centerizeButton.addClickHandler(command);
	}

	@Override
	public void collapseComment() {
		addStyleName("collapsed");
	}

	@Override
	public void expandComment() {
		removeStyleName("collapsed");
	}

	@Override
	public void collapseCommentText() {
		text.addStyleName("collapsed");
		bottombar.addStyleName("collapsed");
	}

	@Override
	public void expandCommentText() {
		text.removeStyleName("collapsed");
		bottombar.removeStyleName("collapsed");
	}

	public String getAuthor() {
		return author.getText();
	}

	public String getCommentText() {
		return text.getText();
	}

}
