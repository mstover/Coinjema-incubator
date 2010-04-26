package com.coinjema.client;

import com.coinjema.client.comments.ClientUser;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserServiceAsync {

	public void login(String username, String password,
			AsyncCallback<ClientUser> callback);

	public void register(String username, String password,
			AsyncCallback<ClientUser> callback);

	public void logout(AsyncCallback<Boolean> callback);

	public void getCurrentUser(AsyncCallback<ClientUser> callback);

	public void getUserTimeOnStory(long currentStory,
			AsyncCallback<Long> asyncCallback);
}
