package de.hdm.gruppe2.server;

import de.hdm.gruppe2.server.db.ChatMapper;
import de.hdm.gruppe2.server.db.HashtagMapper;
import de.hdm.gruppe2.server.db.HashtagSubscriptionMapper;
import de.hdm.gruppe2.server.db.MessageMapper;
import de.hdm.gruppe2.server.db.UserMapper;
import de.hdm.gruppe2.server.db.UserSubscriptionMapper;
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
 * <p>
 * Implementierungsklasse des Interface <code>MsgService</code>. Diese
 * Klasse ist <em>die</em> Klasse, die neben {@link ReportGeneratorImpl}
 * sämtliche Applikationslogik (oder engl. Business Logic) aggregiert. Sie ist
 * wie eine Spinne, die sämtliche Zusammenhänge in ihrem Netz (in unserem Fall
 * die Daten der Applikation) überblickt und für einen geordneten Ablauf und
 * dauerhafte Konsistenz der Daten und Abläufe sorgt.
 * </p>
 * <p>
 * Die Applikationslogik findet sich in den Methoden dieser Klasse. Jede dieser
 * Methoden kann als <em>Transaction Script</em> bezeichnet werden. Dieser Name
 * lässt schon vermuten, dass hier analog zu Datenbanktransaktion pro
 * Transaktion gleiche mehrere Teilaktionen durchgeführt werden, die das System
 * von einem konsistenten Zustand in einen anderen, auch wieder konsistenten
 * Zustand überführen. Wenn dies zwischenzeitig scheitern sollte, dann ist das
 * jeweilige Transaction Script dafür verwantwortlich, eine Fehlerbehandlung
 * durchzuführen.
 * </p>
 * <p>
 * Diese Klasse steht mit einer Reihe weiterer Datentypen in Verbindung. Dies
 * sind:
 * <ol>
 * <li>{@link MsgService}: Dies ist das <em>lokale</em> - also
 * Server-seitige - Interface, das die im System zur Verfügung gestellten
 * Funktionen deklariert.</li>
 * <li>{@link MsgServiceAsync}: <code>MsgServiceImpl</code> und
 * <code>MsgService</code> bilden nur die Server-seitige Sicht der
 * Applikationslogik ab. Diese basiert vollständig auf synchronen
 * Funktionsaufrufen. Wir müssen jedoch in der Lage sein, Client-seitige
 * asynchrone Aufrufe zu bedienen. Dies bedingt ein weiteres Interface, das in
 * der Regel genauso benannt wird, wie das synchrone Interface, jedoch mit dem
 * zusätzlichen Suffix "Async". Es steht nur mittelbar mit dieser Klasse in
 * Verbindung. Die Erstellung und Pflege der Async Interfaces wird durch das
 * Google Plugin semiautomatisch unterstützt. Weitere Informationen unter
 * {@link MsgServiceAsync}.</li>
 * <li> {@link RemoteServiceServlet}: Jede Server-seitig instantiierbare und
 * Client-seitig über GWT RPC nutzbare Klasse muss die Klasse
 * <code>RemoteServiceServlet</code> implementieren. Sie legt die funktionale
 * Basis für die Anbindung von <code>MsgServiceImpl</code> an die Runtime
 * des GWT RPC-Mechanismus.</li>
 * </ol>
 * </p>
 * <p>
 * <b>Wichtiger Hinweis:</b> Diese Klasse bedient sich sogenannter
 * Mapper-Klassen. Sie gehören der Datenbank-Schicht an und bilden die
 * objektorientierte Sicht der Applikationslogik auf die relationale
 * organisierte Datenbank ab. Zuweilen kommen "kreative" Zeitgenossen auf die
 * Idee, in diesen Mappern auch Applikationslogik zu realisieren. Einzig nachvollziehbares
 * Argument für einen solchen Ansatz ist die Steigerung der Performance
 * umfangreicher Datenbankoperationen. Doch auch dieses Argument zieht nur dann,
 * wenn wirklich große Datenmengen zu handhaben sind. In einem solchen Fall
 * würde man jedoch eine entsprechend erweiterte Architektur realisieren, die
 * wiederum sämtliche Applikationslogik in der Applikationsschicht isolieren
 * würde. Also, keine Applikationslogik in die Mapper-Klassen "stecken" sondern
 * dies auf die Applikationsschicht konzentrieren!
 * </p>
 * <p>
 * Beachten Sie, dass sämtliche Methoden, die mittels GWT RPC aufgerufen werden
 * können ein <code>throws IllegalArgumentException</code> in der
 * Methodendeklaration aufweisen. Diese Methoden dürfen also Instanzen von
 * {@link IllegalArgumentException} auswerfen. Mit diesen Exceptions können z.B.
 * Probleme auf der Server-Seite in einfacher Weise auf die Client-Seite
 * transportiert und dort systematisch in einem Catch-Block abgearbeitet werden.
 * </p>
 * <p>
 * Es gibt sicherlich noch viel mehr über diese Klasse zu schreiben. Weitere
 * Infos erhalten Sie in der Lehrveranstaltung.
 * </p>
 * 
 * @see MsgService
 * @see MsgServiceAsync
 * @see RemoteServiceServlet
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
@SuppressWarnings("serial")
public class MsgServiceImpl extends RemoteServiceServlet implements MsgService {

	/**
	 * Referenz auf den UserMapper, der Userobjekte mit der Datenbank
	 * abgleicht.
	 */
	private UserMapper usermapper = null;
	
	/**
	 * Referenz auf den ChatMapper, der Chatobjekte mit der Datenbank
	 * abgleicht.
	 */
	private ChatMapper chatmapper = null;
	
	/**
	 * Referenz auf den HashtagMapper, der Hashtagobjekte mit der Datenbank
	 * abgleicht.
	 */
	private HashtagMapper hashtagmapper = null;
	
	/**
	 * Referenz auf den MessageMapper, der Messageobjekte mit der Datenbank
	 * abgleicht.
	 */
	private MessageMapper messagemapper = null;
	
	/**
	 * Referenz auf den HashtagSubscriptionMapper, der HashtagSubscriptionobjekte
	 * mit der Datenbank abgleicht.
	 */
	private HashtagSubscriptionMapper hashtagsubscriptionmapper = null;
	
	/**
	 * Referenz auf den UserSubscriptionMapper, der UserSubscriptionobjekte
	 * mit der Datenbank abgleicht.
	 */
	private UserSubscriptionMapper usersubscriptionmapper = null;

  /*
   * Da diese Klasse ein gewisse Größe besitzt - dies ist eigentlich ein
   * Hinweise, dass hier eine weitere Gliederung sinnvoll ist - haben wir zur
   * besseren Übersicht Abschnittskomentare eingefügt. Sie leiten ein Cluster in
   * irgeneinerweise zusammengehöriger Methoden ein. Ein entsprechender
   * Kommentar steht am Ende eines solchen Clusters.
   */

	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Initialisierung
	* ***************************************************************************
	*/
	
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
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Initialisierung
	* ***************************************************************************
	*/
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für User Objekte
	* ***************************************************************************
	*/
	
	/**
	 * Anlegen eines Users
	 */
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

	/**
	 * Speichern eines User Objektes (Update)
	 */
	@Override
	public User saveUser(User user) {
		return this.usermapper.update(user);
	}

	/**
	 * Auslesen aller User
	 */
	@Override
	public ArrayList<User> findAllUser() {
		return this.usermapper.findAllUsers();
	}
	
	/**
	 * Auslesen aller User mit Ausnahme des eingeloggten Users
	 */
	@Override
	public ArrayList<User> findAllUserWithoutLoggedInUser(User user) {
		return this.usermapper.findAllUsersWithoutLoggedInUser(user);
	}
	
	/**
	 * Auslesen des Users mit der genannten Email Adresse
	 */
	@Override
	public User findUserByEmail(String email) {
		return this.usermapper.findUserByEmail(email);
	}
	
	/**
	 * Löschen eines Users
	 */
	@Override
	public void deleteUser(User user) {
		this.usermapper.delete(user);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für User Objekte
	* ***************************************************************************
	*/
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für Chat Objekte
	* ***************************************************************************
	*/
	
	/**
	 * Anlegen eines Chats
	 */
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

	/**
	 * Löschen eines Chats
	 */
	@Override
	public void deleteChat(Chat chat) {
		this.chatmapper.delete(chat);
	}

	/**
	 * Entfernen eines Teilnehmers aus einem Chat
	 */
	@Override
	public void removeChatParticipant(Chat chat, User participant) {
		this.chatmapper.deleteChatParticipant(chat, participant);
	}

	/**
	 * Auslesen aller Chats
	 */
	@Override
	public ArrayList<Chat> findAllChats() {
		return this.chatmapper.findAllChats();
	}

	/**
	 * Auslesen aller Chats eines Users
	 */
	@Override
	public ArrayList<Chat> findAllChatsOfUser(User currentUser) {
		return this.chatmapper.findAllChatsOfUser(currentUser);
	}
	
	/**
	 * Auslesen aller Nachrichten eines Chats
	 */
	@Override
	public ArrayList<Message> findAllMessagesOfChat(Chat selectedChat) {
		return this.chatmapper.getAllMessagesOfChat(selectedChat);
	}

	/**
	 * Auslesen aller Teilnehmer eines Chats
	 */
	@Override
	public ArrayList<User> findAllParticipantsOfChat(Chat selectedChat) {
		return this.chatmapper.findAllParticipantsOfChat(selectedChat);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für Chat Objekte
	* ***************************************************************************
	*/
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für Message Objekte
	* ***************************************************************************
	*/

	/**
	 * Anlegen eines Message Objekts
	 */
	@Override
	public Message createPost(String text, User author, ArrayList<Hashtag> hashtagList) {
		
		Message message = new Message();
		message.setText(text);
		message.setUserId(author.getId());
		message.setHashtagList(hashtagList);
		
		return this.messagemapper.insertPost(message);
	}
	
	/**
	 * Versenden einer Nachricht an einen Chat
	 */
	@Override
	public Message sendMessage(String text, User author, Chat receiver, ArrayList<Hashtag> hashtagList) {
		Message message = new Message();
		message.setText(text);
		message.setUserId(author.getId());
		message.setChatId(receiver.getId());
		message.setHashtagList(hashtagList);
		
		return this.messagemapper.insertMessage(message);
	}

	/**
	 * Auslesen aller Posts eines Users
	 */
	@Override
	public ArrayList<Message> findAllPostsOfUser(int userId) {
		return this.messagemapper.findAllPostsOfUser(userId);
	}

	/**
	 * Löschen einer Message
	 */
	@Override
	public void deleteMessage(int messageId) {
		this.messagemapper.delete(messageId);
	}

	/**
	 * Speichern einer Nachricht (Update)
	 */
	@Override
	public Message saveMessage(Message message) {
		return this.messagemapper.update(message);
	}
	
	/**
	 * Auslesen aller Nachrichten eines Hashtag Abonnements
	 */
	@Override
	public ArrayList<Message> findAllHashtagSubscriptionPosts(int hashtagId) {
		return this.messagemapper.findAllPostsWithHashtag(hashtagId);
	}
	
	/**
	 * Auslesen aller Nachrichten eines Users
	 */
	@Override
	public ArrayList<Message> findAllMessagesOfUser(User user) {
		return this.messagemapper.findAllMessagesOfUser(user);
	}

	/**
	 * Auslesen aller Nachrichten eines Zeitraums
	 */
	@Override
	public ArrayList<Message> findAllMessagesOfPeriod(String start, String end) {
		return this.messagemapper.findAllMessagesOfPeriod(start, end);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für Message Objekte
	* ***************************************************************************
	*/
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für Hashtag Objekte
	* ***************************************************************************
	*/

	/**
	 * Auslesen aller Hashtags
	 */
	@Override
	public ArrayList<Hashtag> findAllHashtags() {
		return this.hashtagmapper.findAllHashtags();
	}
	
	/**
	 * Anlegen eines Hashtag Objekts
	 */
	@Override
	public Hashtag createHashtag(String keyword) {
		Hashtag hashtag = new Hashtag();
		hashtag.setKeyword(keyword);
		
		return this.hashtagmapper.insert(hashtag);
	}

	/**
	 * Löschen eines Hashtag Objekts
	 */
	@Override
	public void deleteHashtag(Hashtag hashtag) {
		this.hashtagmapper.delete(hashtag);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für Hashtag Objekte
	* ***************************************************************************
	*/
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für HashtagSubscription Objekte
	* ***************************************************************************
	*/

	/**
	 * Anlegen eines Hashtag Abonnements
	 */
	@Override
	public void createHashtagSubscription(Hashtag hashtag, User user) {
		this.hashtagsubscriptionmapper.insert(hashtag, user);
	}

	/**
	 * Auslesen aller Hashtag Abonnements eines Users
	 */
	@Override
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user) {
		return this.hashtagsubscriptionmapper.findAllHashtagSubscriptionsOfUser(user);
	}

	/**
	 * Löschen eines Hashtag Abonnements
	 */
	@Override
	public void deleteHashtagSubscription(HashtagSubscription hs) {		
		this.hashtagsubscriptionmapper.delete(hs.getHashtagId(), hs.getRecipientId());		
	}
	
	/**
	 * Auslesen aller Abonnenten eines Hashtags
	 */
	@Override
	public ArrayList<User> findAllFollowersOfHashtag(Hashtag h) {
		return this.hashtagsubscriptionmapper.findAllFollowersOfHashtag(h);
	}

	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für HashtagSubscription Objekte
	* ***************************************************************************
	*/

	/*
	* ***************************************************************************
	* ABSCHNITT, Beginn: Methoden für UserSubscription Objekte
	* ***************************************************************************
	*/
	
	/**
	 * Anlegen eines Nutzer Abonnements
	 */
	@Override
	public void createUserSubscription(User sender, User subscriber) {
		UserSubscription us = new UserSubscription();
		us.setSenderId(sender.getId());
		us.setRecipientId(subscriber.getId());
		
		this.usersubscriptionmapper.insert(us);
	}

	/**
	 * Auslesen aller Nutzer Abonnements eines Users
	 */
	@Override
	public ArrayList<UserSubscription> findAllUserSubscriptionsOfUser(User user) {
		return this.usersubscriptionmapper.findAllUserSubscriptionsOfUser(user);
	}

	/**
	 * Löschen eines Nutzer Abonnements
	 */
	@Override
	public void deleteUserSubscription(UserSubscription us) {
		this.usersubscriptionmapper.delete(us.getSenderId(), us.getRecipientId());
	}
	
	/**
	 * Auslesen aller Abonnenten eines Nutzers
	 */
	@Override
	public ArrayList<User> findAllFollowersOfUser(User u) {
		return this.usersubscriptionmapper.findAllFollowersOfUser(u);
	}
	
	/*
	* ***************************************************************************
	* ABSCHNITT, Ende: Methoden für UserSubscription Objekte
	* ***************************************************************************
	*/
}