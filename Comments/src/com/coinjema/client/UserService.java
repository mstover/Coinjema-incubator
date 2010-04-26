package com.coinjema.client;

import com.coinjema.client.comments.ClientUser;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("user")
public interface UserService extends RemoteService {

	public ClientUser login(String username, String password);

	public ClientUser register(String username, String password);

	public boolean logout();

	public ClientUser getCurrentUser();

	public long getUserTimeOnStory(long currentStory);
}
