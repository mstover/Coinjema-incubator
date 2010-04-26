package com.coinjema.client;

import java.util.List;

import com.coinjema.client.comment.ui.CommentContainer;
import com.coinjema.client.comment.ui.CommentGetter;
import com.coinjema.client.comment.ui.CommentScroller;
import com.coinjema.client.comment.ui.CommentWidget;
import com.coinjema.client.comment.ui.DetectUserActivity;
import com.coinjema.client.comment.ui.MainToolbar;
import com.coinjema.client.comment.ui.StoryToolbar;
import com.coinjema.client.comment.ui.StoryWidget;
import com.coinjema.client.comments.ClientUser;
import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.coinjema.client.event.EventCatcher;
import com.coinjema.client.event.ReplyButtonListener;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Comments implements EntryPoint, ClientCommentSystem {
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final CommentServiceAsync commentService = GWT
			.create(CommentService.class);

	private final UserServiceAsync userService = GWT
			.create(UserService.class);

	public CommentContainer commentContainer;
	private FlowPanel mainPanel;
	private ClientUser user;
	private StoryToolbar storyTools;
	private DetectUserActivity detection;

	private long userTimeOnStory = Long.MAX_VALUE;

	private CommentGetter commentRetrieval;

	public void getComments(long storyId, long mostRecentId,
			AsyncCallback<List<UserComment>> callback) {
		commentService.getComments(storyId, mostRecentId, callback);
	}

	private long latestCommentId = 0;
	private long currentStory = 0;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		try {
			currentStory = Long.parseLong(Window.Location
					.getParameter("story"));
			userTimeOnStory = Long.MAX_VALUE;
		} catch (NumberFormatException e) {

		}
		ClientCodeRegistry.system(this);
		userService.getCurrentUser(new AsyncCallback<ClientUser>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Failed to get user");
				initialize();
			}

			public void onSuccess(ClientUser result) {
				if (result != null) {
					user = result;
				}
				initialize();
			}
		});
	}

	public long getLatestCommentId() {
		return latestCommentId;
	}

	public long getCurrentStoryId() {
		return currentStory;
	}

	private void initialize() {
		commentContainer = new CommentContainer(this);
		commentRetrieval = new CommentGetter(this, commentContainer);
		RootPanel.get("toolbar").add(new MainToolbar(this));
		storyTools = new StoryToolbar(this);
		// RootPanel.get("story-toolbar").add(storyTools);
		ClientCodeRegistry.commentContainer(commentContainer);
		mainPanel = new FlowPanel();
		mainPanel.add(storyTools);
		mainPanel.add(commentContainer);
		CommentScroller commentScroll = new CommentScroller(mainPanel);
		commentScroll.addStyleName("main-content");
		ClientCodeRegistry.mainCommentScroller(commentScroll);
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		commentScroll.setHeight(Window.getClientHeight()
				- RootPanel.get("toolbar").getOffsetHeight() + "px");
		RootPanel.get("commentThreadContainer").add(commentScroll);
		getUserTimeOnStory();
		detection = new DetectUserActivity(this, commentContainer);
		initializeEventHandlers();
	}

	private void initializeEventHandlers() {
		ClientCodeRegistry.eventCatcher(new EventCatcher());
		ClientCodeRegistry.eventCatcher().addListener(
				new ReplyButtonListener());
	}

	public String getSource() {
		return commentContainer.toString();
	}

	@Override
	public void submitComment(long parentId, String text,
			final CommentWidget w) {
		commentService.submitComment(currentStory, parentId, text,
				new AsyncCallback<UserComment>() {
					@Override
					public void onFailure(Throwable caught) {
						if (caught instanceof NotLoggedInException) {
							Window.alert("Not logged in!");
							commentContainer.remove(w);
						}

					}

					@Override
					public void onSuccess(UserComment result) {
						commentContainer.updateWidget(w, result);

					}
				});
	}

	private void getComments() {
		if (Window.Location.getParameter("stream") != null) {
			commentService.getStreamStory(Window.Location
					.getParameter("stream"), Window.Location
					.getParameter("owner"), Window.Location
					.getParameter("path"), Window.Location
					.getParameter("title"), new AsyncCallback<Story>() {

				public void onFailure(Throwable caught) {
					System.out.println("Failed " + caught);
					caught.printStackTrace();
				}

				public void onSuccess(Story result) {
					if (result == null) {
						Window.Location.assign("/Comments.html");
						return;
					}
					currentStory = result.getId();
					commentContainer.clear();
					mainPanel.insert(new StoryWidget(result), 0);
					commentContainer.displayCommentChildren(result, 0);
					commentRetrieval.schedule();
					userService.getUserTimeOnStory(currentStory,
							new AsyncCallback<Long>() {
								@Override
								public void onSuccess(Long result) {
									userTimeOnStory = result;
								}

								@Override
								public void onFailure(Throwable caught) {
								}
							});
				}
			});
		} else if (Window.Location.getParameter("story") == null) {
			commentService.getComments(new AsyncCallback<List<Story>>() {
				@Override
				public void onFailure(Throwable caught) {
					System.out.println("Failed " + caught);
					caught.printStackTrace();
				}

				public void onSuccess(java.util.List<Story> result) {
					commentContainer.clear();
					for (Story c : result) {
						commentContainer.add(commentContainer
								.createTopCommentBox(0, c));
					}
				};
			});
		} else {
			commentService.getComments(currentStory,
					new AsyncCallback<Story>() {
						public void onFailure(Throwable caught) {
							System.out.println("Failed " + caught);
							caught.printStackTrace();
						}

						public void onSuccess(Story result) {
							if (result == null) {
								Window.Location.assign("/Comments.html");
								return;
							}
							commentContainer.clear();
							mainPanel.insert(new StoryWidget(result), 0);
							commentContainer.displayCommentChildren(result,
									0);
							commentRetrieval.schedule();
							userService.getUserTimeOnStory(currentStory,
									new AsyncCallback<Long>() {
										@Override
										public void onSuccess(Long result) {
											userTimeOnStory = result;
										}

										@Override
										public void onFailure(Throwable caught) {
										}
									});
						}
					});
		}
	}

	protected void getUserTimeOnStory() {
		userService.getUserTimeOnStory(currentStory,
				new AsyncCallback<Long>() {
					@Override
					public void onSuccess(Long result) {
						userTimeOnStory = result;
						getComments();
					}

					@Override
					public void onFailure(Throwable caught) {
						getComments();
					}
				});
	}

	@Override
	public void setLatestCommentId(long commentId) {
		latestCommentId = Math.max(latestCommentId, commentId);
	}

	@Override
	public boolean loggedIn() {
		return user != null;
	}

	@Override
	public ClientUser getUser() {
		return user;
	}

	@Override
	public void login(String username, String password) {
		userService.login(username, password,
				new AsyncCallback<ClientUser>() {

					@Override
					public void onSuccess(ClientUser result) {
						user = result;
						refreshMainToolbar();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Login failed");

					}
				});

	}

	@Override
	public void logout() {
		userService.logout(new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				user = null;
				refreshMainToolbar();
			}

			public void onSuccess(Boolean result) {
				user = null;
				refreshMainToolbar();
			};
		});

	}

	@Override
	public void registerUser(String username, String password) {
		userService.register(username, password,
				new AsyncCallback<ClientUser>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Registration failed");
					}

					@Override
					public void onSuccess(ClientUser result) {
						user = result;
						refreshMainToolbar();
					}
				});

	}

	private void refreshMainToolbar() {
		RootPanel.get("toolbar").clear();
		RootPanel.get("toolbar").add(new MainToolbar(Comments.this));
		storyTools.update();
	}

	@Override
	public void submitStory(String storyTitle, String storyText,
			String[] links) {
		commentService.submitStory(storyTitle, storyText, links,
				new AsyncCallback<Story>() {

					@Override
					public void onSuccess(Story result) {
						commentContainer.add(commentContainer
								.createTopCommentBox(0, result));
					}

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	public void scheduleGet() {
		commentRetrieval.schedule();

	}

	@Override
	public long getNewTime() {
		return userTimeOnStory;
	}

	@Override
	public void createStream(String streamTitle, String streamText,
			String link, String homepage) {
		commentService.createStream(streamTitle, link, streamText,
				homepage, new AsyncCallback<Boolean>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Failed to create stream");
					}

					public void onSuccess(Boolean result) {
						Window.alert("Stream Created!");
					};
				});
	}
}
