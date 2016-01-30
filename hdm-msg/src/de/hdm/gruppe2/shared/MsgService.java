package de.hdm.gruppe2.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

/**
 * <p>
 * Synchrone Schnittstelle für eine RPC-fähige Klasse zur Verwaltung von Messenger Aktivitäten.
 * </p>
 * <p>
 * <code>@RemoteServiceRelativePath("msgServlet")</code> ist bei der
 * Adressierung des aus der zugehörigen Impl-Klasse entstehenden
 * Servlet-Kompilats behilflich. Es gibt im Wesentlichen einen Teil der URL des
 * Servlets an.
 * </p>
 * 
 * @author Thies
 */
@RemoteServiceRelativePath("msgServlet")
public interface MsgService extends RemoteService {

  /**
   * Initialisierung des Objekts. Diese Methode ist vor dem Hintergrund von GWT
   * RPC zusätzlich zum No Argument Constructor der implementierenden Klasse
   * {@link MsgServiceImpl} notwendig. Bitte diese Methode direkt nach der
   * Instantiierung aufrufen.
   * 
   * @throws IllegalArgumentException
   */
	public void init() throws IllegalArgumentException;
	
	/**
	 * Einen Anwender anlegen
	 * 
	 * @param email Email-Adresse
	 * @param nickname Nickname des Nutzers
	 * @return Ein fertiges UserObjekt
	 * @throws IllegalArgumentException
	 */
	public User createUser(String email, String nickname) throws IllegalArgumentException;
	
	/**
	 * Die Änderungen an einem Nutzer abspeichern (update)
	 * 
	 * @param user zu sicherndes Objekt
	 * @return Eventuell geänderter User
	 * @throws IllegalArgumentException
	 */
	public User saveUser(User user) throws IllegalArgumentException;
	
  /**
   * Löschen eines übergebenen Nutzers.
   * 
   * @param user der zu löschende Nutzer
   * @throws IllegalArgumentException
   */
	public void deleteUser(User user) throws IllegalArgumentException;
	
	/**
	 * Auslesen sämtlicher Anwender des Messengers.
	 * 
	 * @return ArrayList sämtlicher User
	 * @throws IllegalArgumentException
	 */
	public ArrayList<User> findAllUser() throws IllegalArgumentException;
	
	/**
	 * Auslesen sämtlicher Anwender des Messengers mit Ausnahme des übergebenen Users
	 * 
	 * @param user Der zu ignorierende Nutzer
	 * @return ArrayList sämtlicher passender User
	 * @throws IllegalArgumentException
	 */
	public ArrayList<User> findAllUserWithoutLoggedInUser(User user) throws IllegalArgumentException;
	
	/**
	 * Auslesen eines Nutzers anhand der Email
	 * 
	 * @param email Email des gesuchten Nutzers
	 * @return User Objekt des gesuchten Nutzers
	 * @throws IllegalArgumentException
	 */
	public User findUserByEmail(String email) throws IllegalArgumentException;
	
	/**
	 * Anlegen eines Chats
	 * 
	 * @param participants Eine ArrayList mit User Objekten der teilnehmenden Nutzer
	 * @return Das angelegte Chat Objekt
	 * @throws IllegalArgumentException
	 */
	public Chat createChat(ArrayList<User> participants) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Chat Objekts
	 * 
	 * @param chat Das zu löschende Chat Objekt
	 * @throws IllegalArgumentException
	 */
	public void deleteChat(Chat chat) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Chat-Teilnehmers aus der Teilnehmerliste.
	 * 
	 * @param chat Der Chat aus dem der Teilnehmer entfernt wird
	 * @param participant Der Teilnehmer der entfernt werden soll
	 * @throws IllegalArgumentException
	 */
	public void removeChatParticipant(Chat chat, User participant) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Chat Objekte
	 * 
	 * @return Eine ArrayList aller Chat Objekte
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Chat> findAllChats() throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Chats eines Anwenders
	 * 
	 * @param currentUser Der User nach dessen Chats gesucht werden.
	 * @return Eine ArrayList aller Chats des Nutzers
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Chat> findAllChatsOfUser(User currentUser) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Abonennten eines Hashtags
	 * 
	 * @param h Der Hashtag nach dessen Abonennten gesucht werden soll.
	 * @return Eine ArrayList aller User die dem Hashtag folgen
	 * @throws IllegalArgumentException
	 */
	public ArrayList<User> findAllFollowersOfHashtag(Hashtag h) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Abonennten eines Anwenders
	 * 
	 * @param u Der User nach dessen Abonennten gesucht werden soll.
	 * @return Eine ArrayList aller User die dem User folgen
	 * @throws IllegalArgumentException
	 */
	public ArrayList<User> findAllFollowersOfUser(User u) throws IllegalArgumentException;
	
	/**
	 * Anlegen eines Posts
	 * 
	 * @param text Der enthaltene Text
	 * @param author Das User Objekt des Verfassers der Nachricht
	 * @param hashtagList  Die Liste der enthaltenen Hashtag Objekte
	 * @return Das angelegte Message Objekt
	 * @throws IllegalArgumentException
	 */
	public Message createPost(String text, User author, ArrayList<Hashtag> hashtagList) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Posts eines Anwenders
	 * 
	 * @param userId Die ID des Nutzers nach dessen Posts gesucht wird
	 * @return Eine ArrayList aller Posts des Nutzers
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Message> findAllPostsOfUser(int userId) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Message Objekts
	 * 
	 * @param messageId Die ID der zu löschenden Message
	 * @throws IllegalArgumentException
	 */
	public void deleteMessage(int messageId) throws IllegalArgumentException;
	
	/**
	 * Speichern einer bearbeiteten Nachricht (update)
	 * 
	 * @param message Das zu speichernde Message Objekt
	 * @return Das überarbeitete Message Objekt
	 * @throws IllegalArgumentException
	 */
	public Message saveMessage(Message message) throws IllegalArgumentException;
	
	/**
	 * Senden eines Message Objekts an einen bestimmten Chat
	 *  
	 * @param text Der enthaltene Text der Nachricht
	 * @param author Der Verfasser der Nachricht
	 * @param receiver Der Empfänger Chat
	 * @param hashtagList Eine Liste aller enthaltenen Hashtags der Nachricht
	 * @return Das versendete Message Objekt
	 * @throws IllegalArgumentException
	 */
	public Message sendMessage(String text, User author, Chat receiver, ArrayList<Hashtag> hashtagList) throws IllegalArgumentException;

	/**
	 * Auslesen aller Nachrichten eines Chats
	 * 
	 * @param selectedChat Das auszulesende Chat Objekt
	 * @return Eine ArrayList aller enthaltenen Nachrichten
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Message> findAllMessagesOfChat(Chat selectedChat) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Nachrichten eines Nutzers
	 * 
	 * @param user Das Nutzer Objekt dessen Nachrichten ausgelesen werden sollen
	 * @return Eine ArrayList aller enthaltenen Nachrichten
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Message> findAllMessagesOfUser(User user) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Nachrichten eines bestimmten Zeitraums
	 * 
	 * @param start Startzeitraum mit Format (yyyy-MM-dd hh:mm:ss)
	 * @param end Endzeitraum mit Format (yyyy-MM-dd hh:mm:ss)
	 * @return Eine ArrayList aller Nachrichten der Periode
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Message> findAllMessagesOfPeriod(String start, String end) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Teilnehmer eines Chats
	 * 
	 * @param selectedChat Der Chat dessen Teilnehmer gesucht werden
	 * @return Eine ArrayList aller User Objekte die an dem Chat teilnehmen
	 * @throws IllegalArgumentException
	 */
	public ArrayList<User> findAllParticipantsOfChat(Chat selectedChat) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Hashtags
	 * 
	 * @return Eine ArrayList aller Hashtag Objekte
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Hashtag> findAllHashtags() throws IllegalArgumentException;
	
	/**
	 * Anlegen eines Hashtag Objekts
	 * 
	 * @param keyword Das Schlagwort des anzulegenden Objekts
	 * @return Das angelegte Objekt
	 * @throws IllegalArgumentException
	 */
	public Hashtag createHashtag(String keyword) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Hashtag Objekts
	 * 
	 * @param hashtag Das zu löschende Hashtag Objekt
	 * @throws IllegalArgumentException
	 */
	public void deleteHashtag(Hashtag hashtag) throws IllegalArgumentException;
	
	/**
	 * Anlegen eines Hashtag Abonnements
	 * 
	 * @param hashtag Das zu abonnierende Hashtag
	 * @param user Der abonnierende User
	 * @throws IllegalArgumentException
	 */
	public void createHashtagSubscription(Hashtag hashtag, User user) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Hashtag Abonnements
	 * 
	 * @param hs Das anzulegende Hashtag Abonnement Objekt
	 * @throws IllegalArgumentException
	 */
	public void deleteHashtagSubscription(HashtagSubscription hs) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Hashtag Abonnements eines Nutzers
	 * 
	 * @param user Der User dessen Abonnements gesucht werden
	 * @return Eine ArrayList aller HashtagSubscription Objekte
	 * @throws IllegalArgumentException
	 */
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user) throws IllegalArgumentException;

	/**
	 * Auslesen aller Nachrichten eines HashtagAbonnements
	 * 
	 * @param hashtagId das Hashtag dessen Nachrichten gesucht werden
	 * @return Eine ArrayList aller Message Objekte
	 * @throws IllegalArgumentException
	 */
	public ArrayList<Message> findAllHashtagSubscriptionPosts(int hashtagId) throws IllegalArgumentException;
	
	/**
	 * Anlegen eines User Abonnements
	 * 
	 * @param sender Der abonnierte Nutzer
	 * @param subscriber Der abonnierende Nutzer
	 * @throws IllegalArgumentException
	 */
	public void createUserSubscription(User sender, User subscriber) throws IllegalArgumentException;
	
	/**
	 * Auslesen aller Nutzer Abonnements eines Nutzers
	 * 
	 * @param user Der User dessen Abonnements gesucht werden
	 * @return Eine ArrayList aller UserSubscription Objekte
	 * @throws IllegalArgumentException
	 */	
	public ArrayList<UserSubscription> findAllUserSubscriptionsOfUser(User user) throws IllegalArgumentException;
	
	/**
	 * Löschen eines Nutzer Abonnements
	 * 
	 * @param us Das zu löschende UserSubscription Objekt
	 * @throws IllegalArgumentException
	 */
	public void deleteUserSubscription(UserSubscription us) throws IllegalArgumentException;
}
