package com.coinjema.client;

import java.util.List;

import com.coinjema.client.comment.ui.CommentWidget;
import com.coinjema.client.comments.ClientUser;
import com.coinjema.client.comments.UserComment;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ClientCommentSystem {

	public void setLatestCommentId(long commentId);

	public void submitComment(long parentId, String text,
			CommentWidget w);

	public boolean loggedIn();

	public String getSource();

	public void registerUser(String username, String password);

	public ClientUser getUser();

	public void login(String username, String password);

	public void logout();

	public void submitStory(String storyTitle, String storyText,
			String[] links);

	public long getCurrentStoryId();

	public long getLatestCommentId();

	public void getComments(long currentStoryId, long latestCommentId,
			AsyncCallback<List<UserComment>> asyncCallback);

	public void scheduleGet();

	public long getNewTime();

	public void createStream(String streamTitle, String streamText,
			String link, String homepage);

}
