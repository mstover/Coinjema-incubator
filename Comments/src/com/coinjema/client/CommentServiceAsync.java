package com.coinjema.client;

import java.util.List;

import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface CommentServiceAsync {
	void getComments(AsyncCallback<List<Story>> callback);

	void getComments(long storyId, AsyncCallback<Story> callback);

	void getComments(long storyId, long mostRecentId,
			AsyncCallback<List<UserComment>> callback);

	void submitComment(long storyId, long parentId, String text,
			AsyncCallback<UserComment> callback);

	void submitStory(String title, String text, String[] links,
			AsyncCallback<Story> callback);

	void createStream(String name, String baseUrl, String text,
			String homepage, AsyncCallback<Boolean> callback);

	void getStreamStory(String streamName, String owner, String path,
			String title, AsyncCallback<Story> callback);
}
