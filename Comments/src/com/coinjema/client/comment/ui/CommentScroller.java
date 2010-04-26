/**
 * 
 */
package com.coinjema.client.comment.ui;

import java.util.Iterator;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author mstover
 * 
 */
public class CommentScroller extends ScrollPanel {

	/**
	 * 
	 */
	public CommentScroller() {
	}

	/**
	 * @param child
	 */
	public CommentScroller(Widget child) {
		super(child);
	}

	public void processChildrenInView(WidgetProcessor processor) {
		processChildrenInView(this.getWidget(), processor);
	}

	public void processChildrenOutOfView(WidgetProcessor processor) {
		processChildrenOutOfView(this.getWidget(), processor);

	}

	private void processChildrenOutOfView(Widget w,
			WidgetProcessor processor) {
		if (!isInView(w)) {
			if (w instanceof CommentWidget) {
				processor.processWidget(w);
			}
		}
		if (w instanceof HasWidgets && !(w instanceof CommentWidget)) {
			HasWidgets container = (HasWidgets) w;
			Iterator<Widget> children = container.iterator();
			while (children.hasNext()) {
				processChildrenOutOfView(children.next(), processor);
			}
		}
	}

	private void processChildrenInView(Widget w,
			WidgetProcessor processor) {
		if (isInView(w)) {
			if (w instanceof CommentWidget) {
				processor.processWidget(w);
			}
		}
		if (w instanceof HasWidgets && !(w instanceof CommentWidget)) {
			HasWidgets container = (HasWidgets) w;
			if (w.getAbsoluteTop() < Window.getClientHeight()) {
				Iterator<Widget> children = container.iterator();
				while (children.hasNext()) {
					processChildrenInView(children.next(), processor);
				}
			}
		}
	}

	private boolean isInView(Widget w) {
		return w.getAbsoluteTop() >= 0
				&& (w.getAbsoluteTop() + w.getOffsetHeight() <= Window
						.getClientHeight());
	}

}
