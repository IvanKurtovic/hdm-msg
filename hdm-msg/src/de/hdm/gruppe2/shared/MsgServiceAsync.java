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

public interface MsgServiceAsync {

	// User Methoden
	void createUser(User user, AsyncCallback<User> callback);
	void deleteUser(User user, AsyncCallback<Void> callback);
	void getAllUser(AsyncCallback<Vector<User>> callback);
	void getUserByGoogleId(String googleId, AsyncCallback<User> callback);
	void saveUser(User user, AsyncCallback<Void> callback);
	
	// Login Methoden
	void getUserInfo(String uri, AsyncCallback<LoginInfo> callback);
	void setLoginInfo(LoginInfo loginInfo, AsyncCallback<Void> callback);
	
	// Message Methoden
	void createMessage(Message message, AsyncCallback<Message> callback);
	void saveMessage(Message message, AsyncCallback<Void> callback);
	void deleteMessage(Message message, AsyncCallback<Void> callback);
	void getMessageById(int id, AsyncCallback<Message> callback);
	void getMessageByUserAndTime(User user, Timestamp startTime,
			Timestamp endTime, AsyncCallback<Message> callback);
	
	// Chat Methoden
	void createChat(Chat chat, AsyncCallback<Chat> callback);
	void saveChat(Chat chat, AsyncCallback<Void> callback);
	void deleteChat(Chat chat, AsyncCallback<Void> callback);
	void getChatById(int id, AsyncCallback<Chat> callback);
	void getChatByUser(User user, AsyncCallback<Chat> callback);
	
	// Hashtag Methoden
	void createHashtag(Hashtag hashtag, AsyncCallback<Hashtag> callback);
	void saveHashtag(Hashtag hashtag, AsyncCallback<Void> callback);
	void deleteHashtag(Hashtag hashtag, AsyncCallback<Void> callback);
	void getAllHashtags(AsyncCallback<Vector<Hashtag>> callback);
	void getHashtagById(int id, AsyncCallback<Hashtag> callback);
	
	// Hashtag Methoden
	void createHashtagAbo(HashtagAbo hashtagAbo,
			AsyncCallback<HashtagAbo> callback);
	void createUserAbo(UserAbo userAbo, AsyncCallback<UserAbo> callback);
	void saveHashtagAbo(HashtagAbo hashtagAbo, AsyncCallback<Void> callback);
	
	// Abo Methoden
	void saveUserAbo(UserAbo userAbo, AsyncCallback<Void> callback);
	void deleteHashtagAbo(HashtagAbo hashtagAbo, AsyncCallback<Void> callback);
	void deleteUserAbo(UserAbo userAbo, AsyncCallback<Void> callback);
	void getAboByUser(User user, AsyncCallback<UserAbo> callback);
	void getAboByHashtag(Hashtag hashtag, AsyncCallback<HashtagAbo> callback);

}
