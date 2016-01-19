package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MsgServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void nutzerAktualisieren(User n, AsyncCallback<Void> asyncCallback);

	void nutzerLoeschen(User user, AsyncCallback<Void> asyncCallback);
}
