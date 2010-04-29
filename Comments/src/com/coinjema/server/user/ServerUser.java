/**
 * 
 */
package com.coinjema.server.user;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author mstover
 * 
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent
	private String userName;

	@Persistent
	byte[] password;

	@Persistent
	private HashMap<Long, Long> timeOnStories;

	public ServerUser() {

	}

	/**
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * 
	 */
	public ServerUser(String username, String password)
			throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		this.userName = username;
		this.password = convertPassword(password);
	}

	private byte[] convertPassword(String p)
			throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		return MessageDigest.getInstance("MD5").digest(
				p.getBytes("utf-8"));
	}

	public String getUserName() {
		return userName;
	}

	public synchronized void setTimeOnStory(long storyId, long time) {
		if (timeOnStories == null) {
			timeOnStories = new HashMap<Long, Long>();
		}
		timeOnStories.put(storyId, time);
	}

	public boolean checkPassword(String password)
			throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		byte[] converted = convertPassword(password);
		for (int i = 0; i < converted.length; i++) {
			if (converted[i] != this.password[i]) {
				return false;
			}
		}
		return true;
	}

	public long getTimeOnStory(long currentStory) {
		if (timeOnStories != null) {
			Long v = timeOnStories.get(currentStory);
			if (v != null) {
				return v;
			}
		}
		return Long.MAX_VALUE;

	}

}
