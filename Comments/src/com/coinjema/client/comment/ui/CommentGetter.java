package com.coinjema.client.comment.ui;

import java.util.ArrayList;
import java.util.List;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.ClientCommentSystem;
import com.coinjema.client.comments.UserComment;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class CommentGetter extends Timer {

	private static final int COMMENT_GET_TIMEOUT = 10000;
	private ClientCommentSystem system;
	private CommentContainer commentContainer;
	public List<UserComment> leftOverComments = new ArrayList<UserComment>();
	private boolean scheduled = false;

	public CommentGetter(ClientCommentSystem system,
			CommentContainer cc) {
		this.system = system;
		this.commentContainer = cc;
	}

	public void schedule() {
		if (!scheduled) {
			scheduled = true;
			schedule(COMMENT_GET_TIMEOUT);
		}
	}

	@Override
	public void run() {
		Window.setStatus("Getting new comments");
		system.getComments(system.getCurrentStoryId(), system
				.getLatestCommentId(),
				new AsyncCallback<List<UserComment>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(List<UserComment> result) {
						int curPos = ClientCodeRegistry
								.mainScrollPosition();
						int windowSize = Window.getClientHeight();
						result.addAll(leftOverComments);
						leftOverComments.clear();
						if (result.size() > 0) {
							int index = 0;
							for (UserComment c : result) {
								if (commentContainer.contains(c)) {
									system.setLatestCommentId(c
											.getCreationTime());
									continue;
								}
								Widget w = null;
								boolean notFound = true;
								int counter = 0;
								if (commentContainer.getWidgetCount() > 0) {
									do {
										w = commentContainer.getWidget(index);
										if (w instanceof CommentWidget) {
											CommentWidget comWidg = (CommentWidget) w;
											if (comWidg.getId() == c
													.getParentId()) {
												curPos = commentContainer
														.insertNewComment(
																leftOverComments,
																curPos, windowSize,
																index, c, comWidg);
												notFound = false;
											}
										}
										index = (index + 1)
												% commentContainer
														.getWidgetCount();
										counter++;
									} while (notFound
											&& (counter < commentContainer
													.getWidgetCount()));
								}
								if (notFound) {
									if (c.getParentId() != -1) {
										leftOverComments.add(c);
									} else {
										commentContainer.add(commentContainer
												.createCommentBox(0, c));
									}
								}
							}
						}
						Window.setStatus("done");
						scheduled = false;
					}

				});
	}
}
