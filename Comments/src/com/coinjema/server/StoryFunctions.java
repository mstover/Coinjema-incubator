package com.coinjema.server;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.coinjema.client.comments.Story;
import com.coinjema.client.comments.UserComment;
import com.coinjema.server.indexing.CommentCollection;
import com.coinjema.server.jdo.CommentJDO;
import com.coinjema.server.jdo.PMF;

public class StoryFunctions {

	private static final long REFRESH_INTERVAL = 1000 * 60;

	public static void refreshStory(CommentCollection cc, Story s) {
		long curTime = System.currentTimeMillis();

		if (s.lastRefresh < (curTime - REFRESH_INTERVAL)) {
			getNewComments(cc, s, curTime);
		}
	}

	private static void getNewComments(CommentCollection cc, Story s,
			long curTime) {
		synchronized (s) {
			if (s.lastRefresh > curTime - REFRESH_INTERVAL) {
				return;
			}
			retrieveAllComments(cc, s);
		}
	}

	public static void integrateChildren(CommentCollection cc,
			Story story, LinkedList<UserComment> children) {
		while (children.size() > 0) {
			UserComment c = children.removeLast();
			if (cc.getComment(c.getId()) != null) {
				continue;
			}
			if (c.getParentId() < 0) {
				story.addChild(c);
			} else {
				Iterator<UserComment> desc = children
						.descendingIterator();
				while (desc.hasNext()) {
					UserComment uc = desc.next();
					if (uc.getId() == c.getParentId()) {
						if (cc.getComment(uc.getId()) != null) {
							cc.getComment(uc.getId()).addChild(c);
						} else {
							uc.addChild(c);
						}
						break;
					}
				}
			}
			cc.addComment(c);
		}
	}

	private static void retrieveAllComments(CommentCollection cc,
			Story story) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(CommentJDO.class);
		q.setFilter("storyId == " + story.getId()
				+ " && creationTime >= " + story.lastRefresh);
		q.setOrdering("creationTime");
		try {
			story.lastRefresh = System.currentTimeMillis();
			List<CommentJDO> comments = (List<CommentJDO>) q.execute();
			LinkedList<UserComment> children = new LinkedList<UserComment>();
			for (CommentJDO c : comments) {
				UserComment comment = new UserComment(c.getAuthor(), c
						.getText(), c.getCreationTime(), c.getStoryId(), c
						.getParentId(), c.getId());
				children.add(comment);
			}
			integrateChildren(cc, story, children);
		} finally {
			q.closeAll();
		}
	}

}
