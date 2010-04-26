package com.coinjema.client;

import java.util.List;

import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("browse")
public interface CommentService extends RemoteService {
	List<Story> getComments();

	Story getComments(long storyId);

	List<UserComment> getComments(long storyId, long mostRecentId);

	UserComment submitComment(long storyId, long parentId, String text)
			throws NotLoggedInException;

	Story submitStory(String title, String text, String[] links)
			throws NotLoggedInException;

	public boolean createStream(String name, String baseUrl,
			String text, String homepage) throws NotLoggedInException;

	public Story getStreamStory(String streamName, String owner,
			String path, String title);
}
