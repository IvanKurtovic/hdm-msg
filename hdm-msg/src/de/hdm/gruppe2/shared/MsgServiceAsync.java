package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The async counterpart of <code>MsgService</code>.
 */
public interface MsgServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void createUser(String email, String firstName, String lastName, AsyncCallback<User> callback);

	void saveUser(User user, AsyncCallback<User> callback);

	void findAllUser(AsyncCallback<ArrayList<User>> callback);
}
