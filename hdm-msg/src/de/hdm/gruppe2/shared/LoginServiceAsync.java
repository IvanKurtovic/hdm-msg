package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	
	void login(String requestUri, AsyncCallback<LoginInfo> callback);
	
}
