package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface MsgService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	void nutzerAktualisieren(User n);

	void nutzerLoeschen(User user);
}
