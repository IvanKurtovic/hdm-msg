package de.hdm.gruppe2.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MsgServiceAsync {

	void createUser(User user, AsyncCallback<User> callback);
	void saveUser(User user, AsyncCallback<Void> callback);
	void deleteUser(User user, AsyncCallback<Void> callback);
	void getAllUser(AsyncCallback<Vector<User>> callback);
	void getUserByGoogleId(String googleId, AsyncCallback<User> callback);
	
}
