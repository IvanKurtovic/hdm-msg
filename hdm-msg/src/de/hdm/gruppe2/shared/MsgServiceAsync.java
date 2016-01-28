package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

/**
 * The async counterpart of <code>MsgService</code>.
 */
public interface MsgServiceAsync {

	void createUser(String email, String nickname, AsyncCallback<User> callback);

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

	void saveMessage(Message message, AsyncCallback<Message> callback);

	void findUserByEmail(String email, AsyncCallback<User> callback);

	void sendMessage(String text, User author, Chat receiver, ArrayList<Hashtag> hashtagList,
			AsyncCallback<Message> callback);

	void findAllMessagesOfChat(Chat selectedChat, AsyncCallback<ArrayList<Message>> callback);

	void findAllParticipantsOfChat(Chat selectedChat, AsyncCallback<ArrayList<User>> callback);

	void findAllHashtags(AsyncCallback<ArrayList<Hashtag>> callback);

	void createHashtagSubscription(Hashtag hashtag, User user, AsyncCallback<Void> callback);

	void deleteHashtagSubscription(HashtagSubscription hs, AsyncCallback<Void> callback);
	
	void findAllHashtagSubscriptionsOfUser(User user, AsyncCallback<ArrayList<HashtagSubscription>> callback);

	void findAllHashtagSubscriptionPosts(int hashtagId, AsyncCallback<ArrayList<Message>> callback);

	void createHashtag(String keyword, AsyncCallback<Hashtag> callback);

	void deleteHashtag(Hashtag hashtag, AsyncCallback<Void> callback);

	void createUserSubscription(User sender, User subscriber, AsyncCallback<Void> callback);

	void findAllUserSubscriptionsOfUser(User user, AsyncCallback<ArrayList<UserSubscription>> callback);

	void deleteUserSubscription(UserSubscription us, AsyncCallback<Void> callback);
}
