package com.coinjema.server.jdo;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.coinjema.client.comments.UserComment;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class CommentJDO {

	public String getText() {
		if (commentText != null) {
			return commentText.getValue();
		} else {
			return "";
		}
	}

	public long getParentId() {
		return parentId;
	}

	public long getStoryId() {
		return storyId;
	}

	public String getAuthor() {
		return author;
	}

	public long getCreationTime() {
		return creationTime;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private Text commentText;

	@Persistent
	private long parentId;

	@Persistent
	private long storyId;

	@Persistent
	private String author;

	@Persistent
	private long creationTime;

	public CommentJDO(UserComment uc) {
		this.commentText = new Text(uc.getText());
		this.author = uc.getAuthor();
		this.parentId = uc.getParentId();
		this.storyId = uc.getStoryId();
		this.creationTime = uc.getCreationTime();
	}

	public long getId() {
		return id;
	}

}
