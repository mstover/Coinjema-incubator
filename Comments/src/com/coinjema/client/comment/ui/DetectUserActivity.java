package com.coinjema.client.comment.ui;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.ClientCommentSystem;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;

public class DetectUserActivity implements ScrollHandler {

	private ClientCommentSystem system;
	private CommentContainer commentContainer;
	private NewCommentProcessor processor = new NewCommentProcessor();
	private OutOfViewProcessor outOfView = new OutOfViewProcessor();
	private ProcessSchedule processTimer = new ProcessSchedule();

	public DetectUserActivity(ClientCommentSystem system,
			CommentContainer cc) {
		this.system = system;
		this.commentContainer = cc;
		ClientCodeRegistry.addMainScrollHandler(this);
	}

	@Override
	public void onScroll(
			com.google.gwt.event.dom.client.ScrollEvent event) {
		system.scheduleGet();
		processTimer.schedule();
	}

	private class OutOfViewProcessor implements WidgetProcessor {

		@Override
		public void processWidget(Widget w) {
			if (w instanceof UserCommentWidget) {
				UserCommentWidget comment = (UserCommentWidget) w;
				if (comment.isNewComment() && comment.isInView()) {
					comment.setNewComment(false);
					comment.setInView(false);
				}
			}
		}
	}

	private class NewCommentProcessor implements WidgetProcessor {

		@Override
		public void processWidget(Widget w) {
			if (w instanceof UserCommentWidget) {
				UserCommentWidget comment = (UserCommentWidget) w;
				if (comment.isNewComment() && !comment.isInView()) {
					comment.setInView(true);
				}
			}
		}
	}

	private class ProcessSchedule extends Timer {
		private boolean scheduled = false;

		@Override
		public void run() {
			ClientCodeRegistry.getMainCommentScroller()
					.processChildrenInView(processor);
			ClientCodeRegistry.getMainCommentScroller()
					.processChildrenOutOfView(outOfView);
			scheduled = false;
		}

		public void schedule() {
			if (scheduled) {
				this.cancel();
				schedule(5000);
			} else {
				scheduled = true;
				schedule(5000);
			}
		}
	}

}
