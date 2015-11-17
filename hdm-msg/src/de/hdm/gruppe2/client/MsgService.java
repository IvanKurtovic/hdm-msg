package de.hdm.gruppe2.client;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface MsgService extends RemoteService {

	User createUser(User user) throws IllegalArgumentException;
	void saveUser(User user) throws IllegalArgumentException;
	void deleteUser(User user) throws IllegalArgumentException;
	Vector<User> getAllUser() throws IllegalArgumentException;
	User getUserByGoogleId(String googleId) throws IllegalArgumentException;
	
}
