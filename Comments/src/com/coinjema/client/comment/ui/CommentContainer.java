/**
 * 
 */
package com.coinjema.client.comment.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coinjema.client.ClientCodeRegistry;
import com.coinjema.client.ClientCommentSystem;
import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.Submission;
import com.coinjema.client.comments.UserComment;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author mstover
 * 
 */
public class CommentContainer extends FlowPanel {

	private ClickHandler collapseHandler = new CollapseHandler();

	private CenterOnComment centerizeHandler;

	private ClientCommentSystem system;

	private Set<Object> comments = new HashSet<Object>();

	/**
	 * 
	 */
	public CommentContainer(ClientCommentSystem system) {
		this.system = system;
		centerizeHandler = new CenterOnComment(this);
	}

	public int getNextSibling(int index, CommentWidget comWidg) {
		for (; index < getWidgetCount(); index++) {
			Widget w = getWidget(index);
			if ((w instanceof CommentWidget)
					&& (((CommentWidget) w).getLevel() <= comWidg
							.getLevel())) {
				return index;
			}
		}
		return getWidgetCount();
	}

	public void collapseSubComments(CommentWidget w) {
		int i = findWidgetIndex(w) + 1;
		for (; i < getWidgetCount(); i++) {
			Widget wi = getWidget(i);
			if ((wi instanceof CommentWidget)
					&& (((CommentWidget) wi).getLevel() > w.getLevel())) {
				((CommentWidget) wi).collapseComment();
			} else {
				break;
			}
		}
	}

	public int findWidgetIndex(CommentWidget w, int start,
			boolean ascending) {
		int i = start;
		int change = i == 0 ? 1 : i == getWidgetCount() ? -1
				: ascending ? 1 : -1;
		for (; (i < getWidgetCount()) && (i >= 0); i += change) {
			if (w.getId() == ((CommentWidget) getWidget(i)).getId()) {
				break;
			}
		}
		return i;
	}

	public int findWidgetIndex(CommentWidget w) {
		return findWidgetIndex(w, 0, true);
	}

	public void expandSubComments(CommentWidget w) {
		int i = findWidgetIndex(w) + 1;
		for (; i < getWidgetCount(); i++) {
			Widget wi = getWidget(i);
			if ((wi instanceof CommentWidget)
					&& (((CommentWidget) wi).getLevel() > w.getLevel())) {
				((CommentWidget) wi).expandComment();
			} else {
				break;
			}
		}
	}

	public void displayCommentChildren(Submission parent, int level) {
		if ((parent != null) && (parent.getChildren() != null)) {
			for (UserComment child : parent.getChildren()) {
				CommentWidget p = createCommentBox(level, child);
				add(p);
				displayCommentChildren(child, level + 1);
			}
		}
	}

	public boolean contains(UserComment c) {
		return comments.contains(c.getId());
	}

	public CommentWidget createTopCommentBox(int level, Story child) {
		comments.add(child.getId());
		CommentWidget l = new TopCommentWidget(child);
		return l;
	}

	public CommentWidget createCommentBox(int level,
			UserComment child, boolean latestComment) {
		comments.add(child.getId());
		UserCommentWidget l = new UserCommentWidget(child.getText(),
				child.getAuthor(), child.getId(), child.getParentId(),
				level, child.getCreationTime());
		l.addCollapseComment(collapseHandler);
		l.addCenterizeHandler(centerizeHandler);
		if (latestComment) {
			if (child.getCreationTime() > system.getNewTime()) {
				l.setNewComment(true);
			}
			system.setLatestCommentId(child.getCreationTime());
		}
		return l;
	}

	public CommentWidget createCommentBox(int level, UserComment c) {
		return createCommentBox(level, c, true);
	}

	public CommentWidget createCommentBox(String text,
			CommentWidget parent) {
		UserCommentWidget l = new UserCommentWidget(text, system
				.getUser().getName(), -1, parent.getId(), parent
				.getLevel() + 1, System.currentTimeMillis());
		l.addCollapseComment(collapseHandler);
		l.addCenterizeHandler(centerizeHandler);
		return l;
	}

	private class CollapseHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			ToggleButton w = (ToggleButton) event.getSource();
			CommentWidget com = (CommentWidget) w.getParent();
			if (w.isDown()) {
				com.collapseCommentText();
				collapseSubComments(com);
			} else {
				com.expandCommentText();
				expandSubComments(com);
			}
		}
	}

	public void updateWidget(CommentWidget w, UserComment result) {
		if (w == null) {
			w = createCommentBox(0, result, false);
			insert(w, 0);
		} else {
			w.setId(result.getId());
		}
		this.comments.add(result.getId());

	}

	public int insertNewComment(List<UserComment> leftOverComments,
			int curPos, int windowSize, int index, UserComment c,
			CommentWidget comWidg) {
		int insertAt = getNextSibling(index + 1, comWidg);
		if (insertAt == getWidgetCount()) {
			add(createCommentBox(comWidg.getLevel() + 1, c));
		} else {
			Widget parentWidg = getWidget(insertAt - 1);
			int pointOfInsert = parentWidg.getAbsoluteTop()
					+ parentWidg.getOffsetHeight();
			boolean collapsed = comWidg.getStyleName().indexOf(
					"collapsed") > -1;
			if (pointOfInsert < curPos) {
				CommentWidget newWidg = createCommentBox(comWidg
						.getLevel() + 1, c);
				if (collapsed) {
					newWidg.collapseComment();
				}
				insert(newWidg, insertAt);
				curPos += newWidg.getOffsetHeight();
				ClientCodeRegistry.mainScrollTo(curPos);
			} else if ((getWidgetCount() > 15)
					&& (pointOfInsert < curPos + windowSize)) {
				leftOverComments.add(c);
				system.setLatestCommentId(c.getCreationTime());
			} else {
				CommentWidget newWidg = createCommentBox(comWidg
						.getLevel() + 1, c);
				if (collapsed) {
					newWidg.collapseComment();
				}
				insert(newWidg, insertAt);
			}
		}
		return curPos;
	}

	public CommentWidget getWidget(ClickEvent event) {
		Widget w = (Widget) event.getSource();
		Widget parent = w.getParent();
		while ((parent != null) && !(parent instanceof CommentWidget)) {
			w = parent;
			parent = w.getParent();
		}
		return (CommentWidget) parent;
	}

	public List<CommentWidget> getThread(CommentWidget com) {
		List<CommentWidget> thread = new ArrayList<CommentWidget>();
		thread.add(com);
		CommentWidget parent = getParent(com);
		while ((parent != null)
				&& !(parent instanceof TopCommentWidget)) {
			thread.add(parent);
			com = parent;
			parent = getParent(com);
		}
		return thread;
	}

	private CommentWidget getParent(CommentWidget com) {
		for (Widget c : getChildren()) {
			if (((CommentWidget) c).getId() == com.getParentId()) {
				return (CommentWidget) c;
			}
		}
		return null;
	}

	private int stackMove = 0;

	public int moveToTop(CommentWidget w, int from) {
		if (from == 0) {
			stackMove = 0;
		}
		CommentWidget parent = getParent(w);
		int childIndex = findWidgetIndex(w, from, false);
		int parentIndex = parent == null ? -1 : findWidgetIndex(parent,
				childIndex, false);

		if ((childIndex == parentIndex + 1) && (stackMove == 0)) {
			return childIndex;
		}
		int nextSibling = getNextSibling(childIndex + 1, w) - 1;
		int count = 0;
		for (int i = nextSibling; i > childIndex; i--) {
			w = (CommentWidget) getWidget(nextSibling);
			remove(nextSibling);
			insert(w, stackMove);
			count++;
		}
		w = (CommentWidget) getWidget(nextSibling);
		remove(nextSibling);
		insert(w, 0);
		stackMove += count + 1;
		return nextSibling + 1;

	}

	public void ensureVisible(CommentWidget com) {
		CommentScroller scroller = ClientCodeRegistry
				.getMainCommentScroller();
		int top = com.getAbsoluteTop();
		if (top < scroller.getAbsoluteTop()) {
			scroller.setScrollPosition(Math.max(0,
					(scroller.getScrollPosition() - (scroller
							.getAbsoluteTop() - top))
							- (Math.max(0, scroller.getOffsetHeight()
									- com.getOffsetHeight()))));
		} else {
			int comHeight = com.getOffsetHeight();
			if (top + comHeight > scroller.getAbsoluteTop()
					+ scroller.getOffsetHeight()) {
				scroller
						.setScrollPosition(scroller.getScrollPosition()
								+ (top + comHeight
										- scroller.getAbsoluteTop() - scroller
										.getOffsetHeight()));
			}
		}
	}

	private CommentWidget centeredComment;

	public void setCenteredComment(CommentWidget com) {
		if (centeredComment != null) {
			centeredComment.removeStyleName("centered");
		}
		centeredComment = com;
		centeredComment.addStyleName("centered");

	}
}
