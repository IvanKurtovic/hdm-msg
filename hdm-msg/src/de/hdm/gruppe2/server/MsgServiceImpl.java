package de.hdm.gruppe2.server;

import de.hdm.gruppe2.server.db.ChatMapper;
import de.hdm.gruppe2.server.db.HashtagMapper;
import de.hdm.gruppe2.server.db.HashtagSubscriptionMapper;
import de.hdm.gruppe2.server.db.MessageMapper;
import de.hdm.gruppe2.server.db.UserMapper;
import de.hdm.gruppe2.shared.FieldVerifier;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MsgServiceImpl extends RemoteServiceServlet implements MsgService {

	private UserMapper usermapper = UserMapper.usermapper();
	private ChatMapper chatmapper = ChatMapper.chatMapper();
	private HashtagMapper hashtagmapper = HashtagMapper.hashtagMapper();
	private HashtagSubscriptionMapper hashtagsubscriptionmapper = HashtagSubscriptionMapper.hashtagSubscriptionMapper();
	private MessageMapper messagemapper = MessageMapper.messageMapper();

	@Override
	public User createUser(String email, String nickname) {
		
		User u = new User();
		u.setEmail(email);
		u.setNickname(nickname);
		
		/*
	     * Setzen einer vorläufigen User-Id Der insert-Aufruf liefert dann ein
	     * Objekt, dessen Nummer mit der Datenbank konsistent ist.
	     */
		u.setId(1);

		return this.usermapper.insert(u);
	}

	@Override
	public User saveUser(User user) {
		return this.usermapper.update(user);
	}

	@Override
	public ArrayList<User> findAllUser() {
		return this.usermapper.findAllUsers();
	}
	
	@Override
	public ArrayList<User> findAllUserWithoutLoggedInUser(User user) {
		return this.usermapper.findAllUsersWithoutLoggedInUser(user);
	}
	
	@Override
	public User findUserByEmail(String email) {
		return this.usermapper.findUserByEmail(email);
	}
	
	@Override
	public void deleteUser(User user) {
		this.usermapper.delete(user);
	}
	
	@Override
	public Chat createChat(ArrayList<User> participants) {
		
		String chatName = "";
		
		for(User u : participants) {
			chatName += u.getNickname() + " ";
		}
		
		Chat c = new Chat();
		c.setName(chatName);
		c.setMemberList(participants);
		return this.chatmapper.insert(c);		
	}

	@Override
	public void deleteChat(Chat chat) {
		this.chatmapper.delete(chat);
	}
	
	@Override
	public void removeChatParticipant(Chat chat, User participant) {
		this.chatmapper.deleteChatParticipant(chat, participant);
	}
	
	@Override
	public ArrayList<Chat> findAllChats() {
		return this.chatmapper.getAllChats();
	}

	@Override
	public ArrayList<Chat> findAllChatsOfUser(User currentUser) {
		return this.chatmapper.getAllChatsOfUser(currentUser);
	}

	@Override
	public Message createPost(String text, User author, ArrayList<Hashtag> hashtagList) {
		
		Message message = new Message();
		message.setText(text);
		message.setUserId(author.getId());
		message.setHashtagList(hashtagList);
		
		return this.messagemapper.insertPost(message);
	}
	
	@Override
	public Message sendMessage(String text, User author, Chat receiver, ArrayList<Hashtag> hashtagList) {
		Message message = new Message();
		message.setText(text);
		message.setUserId(author.getId());
		message.setChatId(receiver.getId());
		message.setHashtagList(hashtagList);
		
		return this.messagemapper.insertMessage(message);
	}

	@Override
	public ArrayList<Message> findAllPostsOfUser(int userId) {
		return this.messagemapper.findAllPostsOfUser(userId);
	}

	@Override
	public void deleteMessage(int messageId) {
		this.messagemapper.delete(messageId);
	}

	@Override
	public Message saveMessage(Message message) {
		return this.messagemapper.update(message);
	}

	@Override
	public ArrayList<Message> findAllMessagesOfChat(Chat selectedChat) {
		return this.chatmapper.getAllMessagesOfChat(selectedChat);
	}

	@Override
	public ArrayList<User> findAllParticipantsOfChat(Chat selectedChat) {
		return this.chatmapper.getAllParticipantsOfChat(selectedChat);
	}

	@Override
	public ArrayList<Hashtag> findAllHashtags() {
		return this.hashtagmapper.getAllHashtags();
	}

	@Override
	public void createHashtagSubscription(Hashtag hashtag, User user) {
		this.hashtagsubscriptionmapper.insert(hashtag, user);
	}

	@Override
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user) {
		return this.hashtagsubscriptionmapper.findAllHashtagSubscriptionsOfUser(user);
	}

	@Override
	public ArrayList<Message> findAllHashtagSubscriptionPosts(int hashtagId) {
		return this.messagemapper.findAllPostsWithHashtag(hashtagId);
	}

	@Override
	public void deleteHashtagSubscription(HashtagSubscription hs) {		
		this.hashtagsubscriptionmapper.delete(hs.getHashtagId(), hs.getRecipientId());		
	}

	@Override
	public Hashtag createHashtag(String keyword) {
		Hashtag hashtag = new Hashtag();
		hashtag.setKeyword(keyword);
		
		return this.hashtagmapper.insert(hashtag);
	}

	@Override
	public void deleteHashtag(Hashtag hashtag) {
		this.hashtagmapper.delete(hashtag);
	}
}
