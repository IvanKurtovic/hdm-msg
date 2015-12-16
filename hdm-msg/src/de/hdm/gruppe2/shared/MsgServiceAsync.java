package de.hdm.gruppe2.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.User;

public interface MsgServiceAsync {

	void createUser(User user, AsyncCallback<User> callback);
	void deleteUser(User user, AsyncCallback<Void> callback);
	void getAllUser(AsyncCallback<Vector<User>> callback);
	void getUserByGoogleId(String googleId, AsyncCallback<User> callback);
	void saveUser(User user, AsyncCallback<Void> callback);
	
	void getUserInfo(String uri, AsyncCallback<LoginInfo> callback);
	void setLoginInfo(LoginInfo loginInfo, AsyncCallback<Void> callback);

}
