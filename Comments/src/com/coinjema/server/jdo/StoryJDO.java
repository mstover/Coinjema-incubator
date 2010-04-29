package com.coinjema.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.coinjema.client.comments.Story;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StoryJDO {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private String[] links;

	@Persistent
	private String author;

	@Persistent
	private Text storyText;

	@Persistent
	private String title;

	public String[] getLinks() {
		return links;
	}

	public String getAuthor() {
		return author;
	}

	public String getText() {
		if (storyText != null) {
			return storyText.getValue();
		} else {
			return "";
		}
	}

	public String getTitle() {
		return title;
	}

	public long getCreationTime() {
		return creationTime;
	}

	@Persistent
	private long creationTime;

	public StoryJDO(Story st) {
		this.title = st.getTitle();
		this.storyText = new Text(st.getText());
		this.author = st.getAuthor();
		this.links = st.getLinks();
		this.creationTime = st.getCreationTime();
	}

	public long getId() {
		return id;
	}
}
