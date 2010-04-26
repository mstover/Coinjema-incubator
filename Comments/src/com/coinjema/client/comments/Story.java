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
public class Story implements Submission, Serializable {

	transient public volatile long lastRefresh;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] links;

	private String title;

	private String author;

	private String text;

	private long creationTime;

	private List<UserComment> childComments;

	private Long id;

	public String[] getLinks() {
		return links;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setLinks(String[] links) {
		this.links = links;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.comments.Submission#getChildren()
	 */
	public Collection<UserComment> getChildren() {
		return childComments;
	}

	/**
	 * 
	 */
	public Story() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param author
	 * @param t
	 * @param parent
	 */
	public Story(String author, String title, String text,
			String[] links) {
		this.author = author;
		this.text = text;
		this.links = links;
		this.title = title;
		this.creationTime = System.currentTimeMillis();
	}

	public Story(String author, String title, String text,
			String[] links, long creationTime, long id) {
		this.author = author;
		this.text = text;
		this.links = links;
		this.title = title;
		this.creationTime = creationTime;
		this.id = id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public String getAuthor() {
		return author;
	}

	public String getText() {
		return text;
	}

	public Long getId() {
		return id;
	}

	public synchronized void addChild(UserComment child) {
		if (childComments == null) {
			childComments = new ArrayList<UserComment>();
		}
		if (child != null) {
			childComments.add(child);
		}
	}

	public int getCommentCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
