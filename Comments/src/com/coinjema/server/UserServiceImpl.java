/**
 * 
 */
package com.coinjema.server;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import com.coinjema.client.UserService;
import com.coinjema.client.comments.ClientUser;
import com.coinjema.server.indexing.UserList;
import com.coinjema.server.user.ServerUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author mstover
 * 
 */
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	final private UserList users;

	/**
	 * 
	 */
	public UserServiceImpl() {
		users = new UserList();
	}

	@Override
	public boolean logout() {
		getThreadLocalRequest().getSession().removeAttribute("user");
		return true;
	}

	@Override
	public ClientUser getCurrentUser() {
		ServerUser u = (ServerUser) getThreadLocalRequest()
				.getSession().getAttribute("user");
		System.out.println("user = " + u);
		if (u != null) {
			return new ClientUser(u.getUserName());
		}
		return null;
	}

	@Override
	public long getUserTimeOnStory(long currentStory) {
		ServerUser u = (ServerUser) getThreadLocalRequest()
				.getSession().getAttribute("user");

		if (u != null) {
			return u.getTimeOnStory(currentStory);
		} else {
			return Long.MAX_VALUE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.UserService#login(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ClientUser login(String username, String password) {
		ServerUser user;
		try {
			user = users.getServerUser(username);
			if (user.checkPassword(password)) {
				getThreadLocalRequest().getSession(true).setAttribute(
						"user", user);
				System.out.println("just stored user "
						+ getThreadLocalRequest().getSession(true)
								.getAttribute("user"));
				return new ClientUser(user.getUserName());
			} else {
				return null;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.coinjema.client.UserService#register(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ClientUser register(String username, String password) {
		ServerUser user = null;
		try {
			user = users.getServerUser(username);
		} catch (Exception e) {

		}
		if (user != null) {
			return null;
		} else {
			try {
				user = new ServerUser(username, password);
				users.registerUser(user);
				getThreadLocalRequest().getSession(true).setAttribute(
						"user", user);
				return new ClientUser(user.getUserName());
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
