package de.hdm.gruppe2.server;

import de.hdm.gruppe2.server.db.ChatMapper;
import de.hdm.gruppe2.server.db.MessageMapper;
import de.hdm.gruppe2.server.db.UserMapper;
import de.hdm.gruppe2.shared.FieldVerifier;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
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
	private MessageMapper messagemapper = MessageMapper.messageMapper();
	
	
	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid. 
		if (!FieldVerifier.isValidName(input)) {
			// If the input is not valid, throw an IllegalArgumentException back to
			// the client.
			throw new IllegalArgumentException("Name must be at least 4 characters long");
		}

		String serverInfo = getServletContext().getServerInfo();
		String userAgent = getThreadLocalRequest().getHeader("User-Agent");

		// Escape data from the client to avoid cross-site script vulnerabilities.
		input = escapeHtml(input);
		userAgent = escapeHtml(userAgent);

		return "Hello, " + input + "!<br><br>I am running " + serverInfo + ".<br><br>It looks like you are using:<br>"
				+ userAgent;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	@Override
	public User createUser(String email, String firstName, String lastName) {
		
		User u = new User();
		u.setEmail(email);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		
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
	public void deleteUser(User user) {
		this.usermapper.delete(user);
	}
	
	@Override
	public Chat createChat(ArrayList<User> participants) {
		
		String chatName = "";
		
		for(User u : participants) {
			chatName += u.getFirstName() + " ";
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


}
