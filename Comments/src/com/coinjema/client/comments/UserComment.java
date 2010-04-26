/**
 * 
 */
package com.coinjema.client.comments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author mstover
 * 
 */
public class UserComment implements Serializable, Submission {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String text;

	private long parentId;

	private List<UserComment> childComments;

	private String author;

	private long storyId;

	protected void setParentId(long parentId) {
		this.parentId = parentId;
	}

	private long creationTime;

	private long id;

	/**
	 * 
	 */
	public UserComment() {
		// TODO Auto-generated constructor stub
	}

	public UserComment(String author, String text, long creationTime,
			long storyId, long parentId, long id) {
		this.author = author;
		this.text = text;
		this.creationTime = creationTime;
		this.storyId = storyId;
		this.parentId = parentId;
		this.id = id;
	}

	public UserComment(String author, String t, UserComment parent,
			Story story) {
		text = t;
		if (parent != null) {
			parentId = parent.getId();
			parent.addChild(this);
		} else {
			parentId = -1;
		}
		if (story != null) {
			this.storyId = story.getId();
			if (parentId == -1) {
				story.addChild(this);
			}
		}
		this.author = author;
		creationTime = System.currentTimeMillis();
	}

	public void setStoryId(long storyId) {
		this.storyId = storyId;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoryId() {
		return storyId;
	}

	public long getParentId() {
		return parentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.comments.Submission#getChildren()
	 */
	public Collection<UserComment> getChildren() {
		return childComments;
	}

	public long getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.comments.Submission#getAuthor()
	 */
	public String getAuthor() {
		return author;
	}

	public synchronized void addChild(UserComment child) {
		if (childComments == null) {
			childComments = new ArrayList<UserComment>();
		}
		if (child != null) {
			childComments.add(child);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.comments.Submission#getText()
	 */
	public String getText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.comments.Submission#getCommentCount()
	 */
	public int getCommentCount() {
		if (childComments == null) {
			return 0;
		}
		int sum = childComments.size();
		for (Submission child : getChildren()) {
			sum += child.getCommentCount();
		}
		return sum;
	}

	@Override
	public String toString() {
		return author + " id: " + getId() + " parentId: "
				+ getParentId() + "\n" + text;
	}

}
