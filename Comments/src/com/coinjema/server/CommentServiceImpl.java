package com.coinjema.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.coinjema.client.CommentService;
import com.coinjema.client.NotLoggedInException;
import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.coinjema.server.indexing.CommentCollection;
import com.coinjema.server.jdo.PMF;
import com.coinjema.server.jdo.StoryJDO;
import com.coinjema.server.jdo.StoryStream;
import com.coinjema.server.user.ServerUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CommentServiceImpl extends RemoteServiceServlet
		implements CommentService {

	private final static long WEEK = 1000 * 60 * 60 * 24 * 7;

	private long storyRefreshTime = System.currentTimeMillis() - WEEK;

	public List<Story> getComments() {
		CommentCollection commentCollection = getCommentCollection();
		loadStories(commentCollection);
		return commentCollection.getTopComments();
	}

	public Story getComments(long storyId) {
		CommentCollection commentCollection = getCommentCollection();

		Story s = commentCollection.getTopComment(storyId);
		preserveUserTimeOnStory(storyId);
		return s;

	}

	private void preserveUserTimeOnStory(long storyId) {
		try {
			ServerUser u = getCurrentUser();
			PersistenceManager pm = PMF.get().getPersistenceManager();
			ServerUser uu = pm.getObjectById(ServerUser.class, u
					.getUserName());
			uu.setTimeOnStory(storyId, System.currentTimeMillis());
			System.out.println("Persisting time "
					+ uu.getTimeOnStory(storyId));
			pm.makePersistent(uu);
			getThreadLocalRequest().getSession(true).setAttribute(
					"user", uu);
		} catch (NotLoggedInException e) {
			// we don't care
		}
	}

	public synchronized CommentCollection getCommentCollection() {
		CommentCollection cc = (CommentCollection) this
				.getServletContext().getAttribute("comments");
		if (cc == null) {
			cc = new CommentCollection();
			loadStories(cc);
			getServletContext().setAttribute("comments", cc);
		}
		return cc;
	}

	private void loadStories(CommentCollection cc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(StoryJDO.class);
		q.setFilter("creationTime > " + storyRefreshTime);
		storyRefreshTime = System.currentTimeMillis();
		try {
			List<StoryJDO> stories = (List<StoryJDO>) q.execute();
			for (StoryJDO story : stories) {
				Story s = new Story(story.getAuthor(), story.getTitle(),
						story.getText(), story.getLinks(), story
								.getCreationTime(), story.getId());
				cc.addTopComment(s);
			}
		} finally {
			q.closeAll();
		}
	}

	public CommentServiceImpl() {
		super();

	}

	@Override
	public List<UserComment> getComments(long storyId,
			long mostRecentId) {
		CommentCollection commentCollection = getCommentCollection();
		List<UserComment> comments = commentCollection
				.getCommentsAfterTime(mostRecentId, storyId);
		preserveUserTimeOnStory(storyId);
		return comments;

	}

	public UserComment submitComment(long storyId, long parentId,
			String text) throws NotLoggedInException {
		CommentCollection commentCollection = getCommentCollection();
		ServerUser user = getCurrentUser();
		return commentCollection.addComment(user.getUserName(), text,
				parentId, storyId);
	}

	public ServerUser getCurrentUser() throws NotLoggedInException {
		ServerUser user = (ServerUser) getThreadLocalRequest()
				.getSession().getAttribute("user");
		if (user == null) {
			throw new NotLoggedInException("NotLoggedInException");
		}
		return user;
	}

	public Story getStreamStory(String streamName, String owner,
			String path, String title) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		PersistenceManager streamManager = PMF.get()
				.getPersistenceManager();
		StoryStream stream = null;
		Story s = null;
		stream = findStream(streamManager, owner, streamName);
		String url = (!path.startsWith("http://") ? stream.getBaseUrl()
				+ "/" : "")
				+ path;
		CommentCollection cc = getCommentCollection();
		for (long i : stream.getStoryIds()) {
			s = cc.getTopComment(i);
			String link = s.getLinks() != null
					&& s.getLinks().length == 1 ? s.getLinks()[0] : null;
			System.out.println("path = " + path + " link = " + link);
			if (link != null
					&& link.equals((!path.startsWith("http://") ? stream
							.getBaseUrl()
							+ "/" : "")
							+ path)) {
				return s;
			}
		}
		s = cc.addStory(stream.getOwner(), title, "",
				new String[] { url });
		stream.addStory(s.getId());
		System.out.println("stream stories = " + stream.getStoryIds());
		streamManager.makePersistent(stream);
		streamManager.close();
		return s;
	}

	@Override
	public Story submitStory(String title, String text, String[] links)
			throws NotLoggedInException {
		CommentCollection commentCollection = getCommentCollection();
		// TODO Auto-generated method stub
		return commentCollection.addStory(getCurrentUser()
				.getUserName(), title, text, links);
	}

	public boolean createStream(String name, String baseUrl,
			String text, String homepage) throws NotLoggedInException {
		ServerUser user = getCurrentUser();
		if (!baseUrl.startsWith("http://") && baseUrl.length() > 0) {
			baseUrl = "http://" + baseUrl;
		}

		if (!homepage.startsWith("http://")) {
			homepage = "http://" + homepage;
		}
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		}

		StoryStream stream = findStream(null, user.getUserName(), name);
		if (stream != null) {
			return false;
		} else {
			stream = new StoryStream(user.getUserName(), name, baseUrl,
					text, homepage);
			PMF.get().getPersistenceManager().makePersistent(stream);
			return true;
		}
	}

	public StoryStream findStream(PersistenceManager p,
			String userName, String name) {
		PersistenceManager pm = p != null ? p : PMF.get()
				.getPersistenceManager();
		Query q = pm.newQuery(StoryStream.class);
		q.setFilter("name == '" + name + "' && owner == '" + userName
				+ "'");
		List<StoryStream> streams = (List<StoryStream>) q.execute();
		if (streams != null && streams.size() > 0) {
			return streams.get(0);
		} else {
			return null;
		}
	}

}
