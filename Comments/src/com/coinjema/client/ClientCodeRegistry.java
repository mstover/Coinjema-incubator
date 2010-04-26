package com.coinjema.client;

import com.coinjema.client.comment.ui.CommentContainer;
import com.coinjema.client.comment.ui.CommentScroller;
import com.coinjema.client.event.EventCatcher;
import com.google.gwt.event.dom.client.ScrollHandler;

public class ClientCodeRegistry {

	private final static ClientCodeRegistry registry = new ClientCodeRegistry();

	public ClientCodeRegistry() {

	}

	public static CommentContainer commentContainer() {
		return registry.commentContainer;
	}

	public static void commentContainer(CommentContainer cc) {
		registry.commentContainer = cc;
	}

	public static CommentService commentService() {
		return registry.commentService;
	}

	public static void commentService(CommentService cs) {
		registry.commentService = cs;
	}

	public static UserService userService() {
		return registry.userService;
	}

	public static void userService(UserService us) {
		registry.userService = us;
	}

	public static int mainScrollPosition() {
		return registry.mainCommentScroller.getScrollPosition();
	}

	public static void mainScrollTo(int top) {
		registry.mainCommentScroller.setScrollPosition(top);
	}

	public static void mainCommentScroller(CommentScroller sp) {
		registry.mainCommentScroller = sp;
	}

	public static CommentScroller getMainCommentScroller() {
		return registry.mainCommentScroller;
	}

	public static ClientCommentSystem system() {
		return registry.system;
	}

	public static void system(ClientCommentSystem s) {
		registry.system = s;
	}

	private CommentScroller mainCommentScroller;
	private CommentContainer commentContainer;
	private CommentService commentService;
	private UserService userService;
	private EventCatcher eventCatcher;
	private ClientCommentSystem system;

	public static void addMainScrollHandler(
			ScrollHandler detectUserActivity) {
		registry.mainCommentScroller
				.addScrollHandler(detectUserActivity);

	}

	public static EventCatcher eventCatcher() {
		return registry.eventCatcher;
	}

	public static void eventCatcher(EventCatcher eventCatcher) {
		registry.eventCatcher = eventCatcher;
	}
}
