/**
 * 
 */
package com.coinjema.server.indexing;

import java.util.HashMap;
import java.util.Map;

import com.coinjema.server.jdo.PMF;
import com.coinjema.server.user.ServerUser;

/**
 * @author mstover
 * 
 */
public class UserList {

	Map<String, ServerUser> userMap = new HashMap<String, ServerUser>();

	/**
	 * 
	 */
	public UserList() {
		// TODO Auto-generated constructor stub
	}

	public ServerUser getServerUser(String username) {
		ServerUser user = userMap.get(username);
		if (user == null) {
			user = PMF.get().getPersistenceManager().getObjectById(
					ServerUser.class, username);
			if (user != null) {
				userMap.put(user.getUserName(), user);
			}
		}
		return user;
	}

	public void registerUser(ServerUser user) {
		PMF.get().getPersistenceManager().makePersistent(user);
		userMap.put(user.getUserName(), user);

	}

}
