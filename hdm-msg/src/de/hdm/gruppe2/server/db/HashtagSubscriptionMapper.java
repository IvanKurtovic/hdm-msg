package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>HashtagSubscription</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper, HashtagMapper, MessageMapper, UserSubscriptionMapper, ChatMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class HashtagSubscriptionMapper {

  /**
   * Die Klasse HashtagSubscriptionMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see hashtagSubscriptionMapper()
   */
	private static HashtagSubscriptionMapper hashtagSubscriptionMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected HashtagSubscriptionMapper() {}

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>HashtagSubscriptionMapper.hashtagSubscriptionMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>HashtagSubscriptionMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> HashtagSubscriptionMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>HashtagSubscriptionMapper</code>-Objekt.
   * @see hashtagSubscriptionMapper
   */
	public static HashtagSubscriptionMapper hashtagSubscriptionMapper() {
		if(hashtagSubscriptionMapper == null) {
			hashtagSubscriptionMapper = new HashtagSubscriptionMapper();
		}
		
		return hashtagSubscriptionMapper;
	}

  /**
   * Einfügen einer Verlinkung in der Zwischentabelle hashtagsubscription.
   * 
   * @param user Das zu verlinkende User Objekt
   * @param hashtag Das zu verlinkende Hashtag Objekt
   */
	public void insert(Hashtag hashtag, User user) {
		Connection con = DBConnection.connection();
		
		try {
			if(findByHashtagAndUser(hashtag.getId(), user.getId()) == null) {

				Statement stmt = con.createStatement();			
				stmt.executeUpdate("INSERT INTO `dbmessenger`.`hashtagsubscription`(`hashtagId`, `userId`) VALUES (" + hashtag.getId() + ", " + user.getId() + " )");					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

 /**
   * Löschen eines Hashtagabonnements aus der Datenbank.
   * 
   * @param hashtagId Die Id des Hashtags der abonniert wurde
   * @param userId Die Id des abonnierenden Users
   * 
   */
	public void delete(int hashtagId, int userId) {
		Connection con = DBConnection.connection();
		
		try {			
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("DELETE FROM `dbmessenger`.`hashtagsubscription` WHERE `hashtagId` = " 
			+ hashtagId + " AND `userId` = " 
			+ userId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

 /**
   * Suchen eines Hashtagabonnements anhand der ID des abonnierenden und abonnierten Hashtags.
   * 
   * @param hashtagId Die Id des Hashtags der abonniert wurde
   * @param recipientId Die Id des abonnierenden Users
   * @return HashtagSubscription Objekt das das Hashtagabonnement repräsentiert
   */
	public HashtagSubscription findByHashtagAndUser(int hashtagId, int userId) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtagsubscription` WHERE `hashtagId` = " + hashtagId + " AND `userId` = " + userId);
			
			if(rs.next()) {
				HashtagSubscription hs = new HashtagSubscription();
				hs.setRecipientId(rs.getInt("userId"));
				hs.setHashtagId(rs.getInt("hashtagId"));		
				return hs;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

 /**
   * Suchen aller Hashtagabonnements eines Nutzers.
   * 
   * @param user Das NutzerObjekt nach dessen Abonnements gesucht wird
   * @return HashtagSubscription Objekte nach denen gesucht wurde
   */
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user) {
		Connection con = DBConnection.connection();
		ArrayList<HashtagSubscription> subscriptions = new ArrayList<HashtagSubscription>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtagsubscription` WHERE `userId` = " + user.getId());
			
			while(rs.next()) {
				HashtagSubscription hs = new HashtagSubscription();
				hs.setRecipientId(rs.getInt("userId"));
				hs.setHashtagId(rs.getInt("hashtagId"));		
				subscriptions.add(hs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}

 /**
   * Suchen aller Abonnenten eines bestimmten Hashtags.
   * 
   * @param h Das Hashtag nach dessen Abonnenten gesucht wird
   * @return User Objekte nach denen gesucht wurde
   */
	public ArrayList<User> findAllFollowersOfHashtag(Hashtag h) {
		Connection con = DBConnection.connection();
		ArrayList<User> subscriptions = new ArrayList<User>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `user`.`id`, `user`.`email`, `user`.`nickname`, `user`.`creationDate` "
											+ "FROM `dbmessenger`.`user` INNER JOIN `dbmessenger`.`hashtagsubscription` "
											+ "ON `dbmessenger`.`user`.`id` = `dbmessenger`.`hashtagsubscription`.`userId`"
											+ "WHERE `hashtagId` = " + h.getId());
			while(rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setEmail(rs.getString("email"));
				u.setNickname(rs.getString("nickname"));
				u.setCreationDate(rs.getDate("creationDate"));
				subscriptions.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}
}
