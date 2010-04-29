package com.coinjema.client.comments;

import java.util.Collection;

public interface Submission {

	public abstract Collection<UserComment> getChildren();

	public abstract String getAuthor();

	public abstract String getText();

	public abstract int getCommentCount();

	public abstract long getCreationTime();

}