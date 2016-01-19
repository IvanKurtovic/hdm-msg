package de.hdm.gruppe2.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.User;
import java.sql.Timestamp;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagAbo;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserAbo;
/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface MsgService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;

	// User Methoden
		User createUser(User user) throws IllegalArgumentException;
		void saveUser(User user) throws IllegalArgumentException;
		void deleteUser(User user) throws IllegalArgumentException;
		Vector<User> getAllUser() throws IllegalArgumentException;
		User getUserByGoogleId(String googleId) throws IllegalArgumentException;
		
		// Login Methoden
		public LoginInfo getUserInfo(String uri) throws IllegalArgumentException;
		void setLoginInfo (LoginInfo loginInfo) throws IllegalArgumentException;
		
		// Message Methoden
		Message createMessage(Message message) throws IllegalArgumentException;
		void saveMessage(Message message) throws IllegalArgumentException;
		void deleteMessage(Message message) throws IllegalArgumentException;
		Message getMessageById(int id) throws IllegalArgumentException;
		Message getMessageByUserAndTime(User user, Timestamp startTime,
				Timestamp endTime) throws IllegalArgumentException;
		
		// Chat Methoden
		Chat createChat(Chat chat) throws IllegalArgumentException;
		void saveChat(Chat chat) throws IllegalArgumentException;
		void deleteChat(Chat chat) throws IllegalArgumentException;
		Chat getChatById(int id) throws IllegalArgumentException;
		Chat getChatByUser(User user) throws IllegalArgumentException;
		
		// Hashtag Methoden
		Hashtag createHashtag(Hashtag hashtag) throws IllegalArgumentException;
		void saveHashtag(Hashtag hashtag) throws IllegalArgumentException;
		void deleteHashtag(Hashtag hashtag) throws IllegalArgumentException;
		Vector<Hashtag> getAllHashtags() throws IllegalArgumentException;
		Hashtag getHashtagById(int id) throws IllegalArgumentException;
		
		// Abo Methoden
		HashtagAbo createHashtagAbo(HashtagAbo hashtagAbo)
				throws IllegalArgumentException;
		UserAbo createUserAbo(UserAbo userAbo) throws IllegalArgumentException;
		void saveHashtagAbo(HashtagAbo hashtagAbo) throws IllegalArgumentException;
		void saveUserAbo(UserAbo userAbo) throws IllegalArgumentException;
		void deleteHashtagAbo(HashtagAbo hashtagAbo)
				throws IllegalArgumentException;
		void deleteUserAbo(UserAbo userAbo) throws IllegalArgumentException;
		UserAbo getAboByUser(User user) throws IllegalArgumentException;
		HashtagAbo getAboByHashtag(Hashtag hashtag) throws IllegalArgumentException;
	void nutzerAktualisieren(User n);

	void nutzerLoeschen(User user);
}
