/**
 * 
 */
package com.coinjema.server.indexing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jdo.PersistenceManager;

import com.coinjema.client.comments.DisconnectedComment;
import com.coinjema.client.comments.DisconnectedTopComment;
import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.coinjema.server.StoryFunctions;
import com.coinjema.server.jdo.CommentJDO;
import com.coinjema.server.jdo.PMF;
import com.coinjema.server.jdo.StoryJDO;

/**
 * @author mstover
 * 
 */
public class CommentCollection {

	private final Map<Long, Story> topStories = new HashMap<Long, Story>();
	private final Map<Long, UserComment> comments = new HashMap<Long, UserComment>();
	private final CommentTimeBag bag = new CommentTimeBag();

	private static Random rand = new Random();

	/**
	 * 
	 */
	public CommentCollection() {
		// TODO Auto-generated constructor stub
	}

	public UserComment addComment(String author, String text,
			UserComment parent, long storyId) {
		Story top = getTopComment(storyId);
		UserComment c = new UserComment(author, text, parent, top);
		generateId(c);
		comments.put(c.getId(), c);
		bag.addComment(c);
		return c;
	}

	private void generateId(UserComment uc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		CommentJDO jdoComment = pm.makePersistent(new CommentJDO(uc));
		pm.close();
		uc.setId(jdoComment.getId());
	}

	private void generateId(Story s) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StoryJDO jdoStory = pm.makePersistent(new StoryJDO(s));
		pm.close();
		s.setId(jdoStory.getId());
	}

	public UserComment addComment(String author, String text,
			long parentId, long storyId) {
		return addComment(author, text, comments.get(parentId), storyId);
	}

	public Story addStory(String author, String title, String text,
			String[] link) {
		Story top = new Story(author, title, text, link);
		generateId(top);
		addTopComment(top);
		return top;
	}

	public void addTopComment(Story top) {
		if (top != null && !topStories.containsKey(top.getId())) {
			topStories.put(top.getId(), top);
		}
	}

	public UserComment getRandomComment() {
		if (comments.size() == 0) {
			return null;
		}
		int l = Math.abs(rand.nextInt()) % comments.size();
		for (UserComment c : comments.values()) {
			if (l-- == 0) {
				return c;
			}
		}
		return null;
	}

	public Story getTopComment(long storyId) {
		Story s = topStories.get(storyId);
		if (s == null) {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			StoryJDO sjdo = pm.getObjectById(StoryJDO.class, storyId);
			if (sjdo != null) {
				s = new Story(sjdo.getAuthor(), sjdo.getTitle(), sjdo
						.getText(), sjdo.getLinks(),
						sjdo.getCreationTime(), sjdo.getId());
				addTopComment(s);
			}
		}
		StoryFunctions.refreshStory(this, s);
		return s;
	}

	public List<UserComment> getCommentsAfterTime(long mostRecentId,
			Long storyId) {
		List<UserComment> newComments = new ArrayList<UserComment>();
		for (UserComment c : bag.getComments(storyId, mostRecentId)) {
			newComments.add(new DisconnectedComment(c));
		}
		return newComments;
	}

	public List<Story> getTopComments() {
		List<Story> newComments = new ArrayList<Story>();
		for (Story c : topStories.values()) {
			newComments.add(new DisconnectedTopComment(c));
		}
		return newComments;
	}

	public void addComment(UserComment c) {
		if (c != null) {
			comments.put(c.getId(), c);
			bag.addComment(c);
		}
	}

	public Collection<UserComment> getComments(Long storyId, long time) {
		return bag.getComments(storyId, time);
	}

	public UserComment getComment(long id) {
		return comments.get(id);
	}
}
