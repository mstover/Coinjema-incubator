package com.coinjema.server.indexing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.coinjema.client.comments.UserComment;

public class CommentTimeBag {
	
	private Map<Long,StoryBag> storyBags = new HashMap<Long,StoryBag>();
	
	public void addComment(UserComment c) {
		getStoryBag(c.getStoryId()).addComment(c);
	}
	
	private synchronized StoryBag getStoryBag(Long id) {
		StoryBag bag = storyBags.get(id);
		if(bag == null) {
			bag = new StoryBag();
			storyBags.put(id,bag);
		}
		return bag;
	}
	
	public Collection<UserComment> getComments(Long storyId,long time) {
		StoryBag bag = getStoryBag(storyId);
		return bag.tail(time);
	}
	
	private class StoryBag {
		private TreeMap<Long,Set<UserComment>> comments = new TreeMap<Long,Set<UserComment>>();
		
		public synchronized void addComment(UserComment c) {
			Set<UserComment> bag = comments.get(c.getCreationTime());
			if(bag == null) {
				bag = new HashSet<UserComment>();
				comments.put(c.getCreationTime(), bag);
			}
			bag.add(c);
		}
		
		public Collection<UserComment> tail(long time) {
			List<UserComment> list = new ArrayList<UserComment>();
			for(Set<UserComment> set : comments.tailMap(time+1).values()) {
				list.addAll(set);
			}
			return list;
		}
	}

}
