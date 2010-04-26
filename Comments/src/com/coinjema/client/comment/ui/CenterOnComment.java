/**
 * 
 */
package com.coinjema.client.comment.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;

/**
 * @author mstover
 * 
 */
public class CenterOnComment implements ClickHandler {

	CommentContainer container;

	/**
	 * 
	 */
	public CenterOnComment(CommentContainer c) {
		container = c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public void onClick(final ClickEvent event) {
		final DialogBox box = new DialogBox(true, true);
		box.add(new Label("Re-organizing..."));
		box.setTitle("Processing");
		final CommentWidget com = container.getWidget(event);
		container.setCenteredComment(com);
		box.setPopupPosition(Window.getClientWidth() / 3, com
				.getAbsoluteTop());
		box.show();
		new Timer() {
			@Override
			public void run() {
				List<CommentWidget> thread = container.getThread(com);
				int startIndex = 0;
				for (CommentWidget w : thread) {
					startIndex = container.moveToTop(w, startIndex);
				}
				container.ensureVisible(com);
				box.hide();
			}
		}.schedule(1);
	}

}
