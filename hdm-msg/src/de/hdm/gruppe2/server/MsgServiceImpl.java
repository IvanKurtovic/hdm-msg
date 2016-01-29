package de.hdm.gruppe2.server;

import de.hdm.gruppe2.server.db.ChatMapper;
import de.hdm.gruppe2.server.db.HashtagMapper;
import de.hdm.gruppe2.server.db.HashtagSubscriptionMapper;
import de.hdm.gruppe2.server.db.MessageMapper;
import de.hdm.gruppe2.server.db.UserMapper;
import de.hdm.gruppe2.server.db.UserSubscriptionMapper;
import de.hdm.gruppe2.shared.FieldVerifier;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MsgServiceImpl extends RemoteServiceServlet implements MsgService {

	private UserMapper usermapper = null;
	private ChatMapper chatmapper = null;
	private HashtagMapper hashtagmapper = null;
	private MessageMapper messagemapper = null;
	private HashtagSubscriptionMapper hashtagsubscriptionmapper = null;
	private UserSubscriptionMapper usersubscriptionmapper = null;

	/**
	* <p>
	* Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	* <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	* ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
	* Konstruktors ist durch die Client-seitige Instantiierung durch
	* <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	* möglich.
	* </p>
	* <p>
	* Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	* Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	* aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	* </p>
	*/
	public MsgServiceImpl() throws IllegalArgumentException {}
	
	/**
	* Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor.
	* 
	* @see #MsgServiceImpl()
	*/
	@Override
	public void init() throws IllegalArgumentException {
	    /*
	     * Ganz wesentlich ist, dass die BankAdministration einen vollständigen Satz
	     * von Mappern besitzt, mit deren Hilfe sie dann mit der Datenbank
	     * kommunizieren kann.
	     */
		this.usermapper = UserMapper.usermapper();
		this.chatmapper = ChatMapper.chatMapper();
		this.hashtagmapper = HashtagMapper.hashtagMapper();
		this.messagemapper = MessageMapper.messageMapper();
		this.hashtagsubscriptionmapper = HashtagSubscriptionMapper.hashtagSubscriptionMapper();
		this.usersubscriptionmapper = UserSubscriptionMapper.userSubscriptionMapper();
	}
	
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

	@Override
	public void createUserSubscription(User sender, User subscriber) {
		UserSubscription us = new UserSubscription();
		us.setSenderId(sender.getId());
		us.setRecipientId(subscriber.getId());
		
		this.usersubscriptionmapper.insert(us);
	}

	@Override
	public ArrayList<UserSubscription> findAllUserSubscriptionsOfUser(User user) {
		return this.usersubscriptionmapper.findAllUserSubscriptionsOfUser(user);
	}

	@Override
	public void deleteUserSubscription(UserSubscription us) {
		this.usersubscriptionmapper.delete(us.getSenderId(), us.getRecipientId());
	}

	@Override
	public ArrayList<Message> findAllMessagesOfUser(User user) {
		return this.messagemapper.findAllMessagesOfUser(user);
	}
}
