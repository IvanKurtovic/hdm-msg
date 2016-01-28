package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("msgServlet")
public interface MsgService extends RemoteService {

	public User createUser(String email, String nickname);
	
	public User saveUser(User user);
	
	public void deleteUser(User user);
	
	public ArrayList<User> findAllUser();
	
	public ArrayList<User> findAllUserWithoutLoggedInUser(User user);
	
	public User findUserByEmail(String email);
	
	public Chat createChat(ArrayList<User> participants);
	
	public void deleteChat(Chat chat);
	
	public void removeChatParticipant(Chat chat, User participant);
	
	public ArrayList<Chat> findAllChats();
	
	public ArrayList<Chat> findAllChatsOfUser(User currentUser);
	
	public Message createPost(String text, User author, ArrayList<Hashtag> hashtagList);
	
	public ArrayList<Message> findAllPostsOfUser(int userId);
	
	public void deleteMessage(int messageId);
	
	public Message saveMessage(Message message);
	
	public Message sendMessage(String text, User author, Chat receiver, ArrayList<Hashtag> hashtagList);

	public ArrayList<Message> findAllMessagesOfChat(Chat selectedChat);
	
	public ArrayList<User> findAllParticipantsOfChat(Chat selectedChat);
	
	public ArrayList<Hashtag> findAllHashtags();
	
	public Hashtag createHashtag(String keyword);
	
	public void deleteHashtag(Hashtag hashtag);
	
	public void createHashtagSubscription(Hashtag hashtag, User user);
	
	public void deleteHashtagSubscription(HashtagSubscription hs);
	
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user);
	
	public ArrayList<Message> findAllHashtagSubscriptionPosts(int hashtagId);
}
