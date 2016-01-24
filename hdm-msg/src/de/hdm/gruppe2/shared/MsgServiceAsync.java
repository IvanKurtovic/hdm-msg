package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * The async counterpart of <code>MsgService</code>.
 */
public interface MsgServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void createUser(String email, String firstName, String lastName, AsyncCallback<User> callback);

	void saveUser(User user, AsyncCallback<User> callback);

	void deleteUser(User user, AsyncCallback<Void> callback);
	
	void findAllUser(AsyncCallback<ArrayList<User>> callback);

	void createChat(ArrayList<User> participants, AsyncCallback<Chat> callback);

	void deleteChat(Chat chat, AsyncCallback<Void> callback);
	
	void findAllChats(AsyncCallback<ArrayList<Chat>> callback);

	void removeChatParticipant(Chat chat, User participant, AsyncCallback<Void> callback);

	void findAllChatsOfUser(User currentUser, AsyncCallback<ArrayList<Chat>> callback);

	void createPost(String text, User author, ArrayList<Hashtag> hashtagList, AsyncCallback<Message> callback);

	void findAllUserWithoutLoggedInUser(User user, AsyncCallback<ArrayList<User>> callback);

	void findAllPostsOfUser(int userId, AsyncCallback<ArrayList<Message>> callback);

	void deleteMessage(int messageId, AsyncCallback<Void> callback);
}
