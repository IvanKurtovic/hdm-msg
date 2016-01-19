package de.hdm.gruppe2.shared;

import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagAbo;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserAbo;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface MsgServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback) throws IllegalArgumentException;

	void nutzerAktualisieren(User n, AsyncCallback<Void> asyncCallback);

	void nutzerLoeschen(User user, AsyncCallback<Void> asyncCallback);

	void createUser(User user, AsyncCallback<User> callback);

	void createChat(Chat chat, AsyncCallback<Chat> callback);

	void saveUser(User user, AsyncCallback<Void> callback);

	void deleteUser(User user, AsyncCallback<Void> callback);

	void getAllUser(AsyncCallback<Vector<User>> callback);

	void getUserByGoogleId(String googleId, AsyncCallback<User> callback);

	void getUserInfo(String uri, AsyncCallback<LoginInfo> callback);

	void setLoginInfo(LoginInfo loginInfo, AsyncCallback<Void> callback);

	void createMessage(Message message, AsyncCallback<Message> callback);

	void saveMessage(Message message, AsyncCallback<Void> callback);

	void deleteMessage(Message message, AsyncCallback<Void> callback);

	void getMessageById(int id, AsyncCallback<Message> callback);

	void getMessageByUserAndTime(User user, Timestamp startTime, Timestamp endTime, AsyncCallback<Message> callback);

	void saveChat(Chat chat, AsyncCallback<Void> callback);

	void deleteChat(Chat chat, AsyncCallback<Void> callback);

	void getChatById(int id, AsyncCallback<Chat> callback);

	void getChatByUser(User user, AsyncCallback<Chat> callback);

	void createHashtag(Hashtag hashtag, AsyncCallback<Hashtag> callback);

	void saveHashtag(Hashtag hashtag, AsyncCallback<Void> callback);

	void deleteHashtag(Hashtag hashtag, AsyncCallback<Void> callback);

	void getAllHashtags(AsyncCallback<Vector<Hashtag>> callback);

	void getHashtagById(int id, AsyncCallback<Hashtag> callback);

	void createHashtagAbo(HashtagAbo hashtagAbo, AsyncCallback<HashtagAbo> callback);

	void createUserAbo(UserAbo userAbo, AsyncCallback<UserAbo> callback);

	void saveHashtagAbo(HashtagAbo hashtagAbo, AsyncCallback<Void> callback);

	void saveUserAbo(UserAbo userAbo, AsyncCallback<Void> callback);

	void deleteHashtagAbo(HashtagAbo hashtagAbo, AsyncCallback<Void> callback);

	void deleteUserAbo(UserAbo userAbo, AsyncCallback<Void> callback);

	void getAboByUser(User user, AsyncCallback<UserAbo> callback);

	void getAboByHashtag(Hashtag hashtag, AsyncCallback<HashtagAbo> callback);
}
