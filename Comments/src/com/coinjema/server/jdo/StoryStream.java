package com.coinjema.server.jdo;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StoryStream {

	@Persistent
	private String owner;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String name;

	@Persistent
	private String baseUrl;

	@Persistent
	private String homepage;

	@Persistent
	private List<Long> storyIds;

	@Persistent
	private String text;

	public StoryStream(String owner, String name, String baseUrl,
			String text, String homepage) {
		this.owner = owner;
		this.name = name;
		this.text = text;
		this.baseUrl = baseUrl;
		storyIds = new ArrayList<Long>();
		this.homepage = homepage;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addStory(Long storyId) {
		storyIds.add(storyId);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public List<Long> getStoryIds() {
		if (storyIds == null) {
			storyIds = new ArrayList<Long>();
		}
		return storyIds;
	}

}
