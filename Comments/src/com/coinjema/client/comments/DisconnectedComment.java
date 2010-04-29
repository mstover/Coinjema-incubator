/**
 * 
 */
package com.coinjema.client.comments;

import javax.jdo.annotations.Key;


/**
 * @author mstover
 *
 */
public class DisconnectedComment extends UserComment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public DisconnectedComment() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param author
	 * @param t
	 * @param parent
	 */
	public DisconnectedComment(UserComment copy) {
		super(copy.getAuthor(), copy.getText(), null,null);
		setId(copy.getId());
		setParentId(copy.getParentId());
		setStoryId(copy.getStoryId());
		// TODO Auto-generated constructor stub
	}

}
