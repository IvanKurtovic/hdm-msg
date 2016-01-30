package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>Message</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper, ChatMapper, HashtagMapper, HashtagSubscriptionMapper, UserSubscriptionMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class MessageMapper {

  /**
   * Die Klasse MessageMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see messagemapper()
   */
	private static MessageMapper messageMapper = null;
	
  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected MessageMapper() {}
	
  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>MessageMapper.messagemapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>MessageMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> MessageMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>MessageMapper</code>-Objekt.
   * @see messagemapper
   */
	public static MessageMapper messageMapper() {
		if(messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		
		return messageMapper;
	}
	
  /**
   * Einfügen eines <code>Message</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt. Es werden ebenfalls mit der Message verbundene Hashtags verlinkt
   * und gegebenenfalls erzeugt.
   * 
   * @param message das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
	public Message insertMessage(Message message) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zunächst schauen wir nach, welches der momentan höchste
		     * Primärschlüsselwert ist.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `message`");
			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * message erhält den bisher maximalen, nun um 1 inkrementierten
	         * Primärschlüssel.
	         */
	        message.setId(rs.getInt("maxid") + 1);

	        stmt = con.createStatement();

	        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
	        int result = stmt.executeUpdate("INSERT INTO `message` (`id`, `text`, `autorId`, `chatId`) VALUES (" + message.getId() + ", '" + message.getText() + "', " + message.getUserId() + ", " + message.getChatId() + ")");
	        
	        // Nur wenn Hashtags vorhanden sind und die Einfügeoperation erfolgreich war sollen
	        // vorhandene Hashtags angelegt / verlinkt werden.
	        if (result != 0 && message.getHashtagList() != null) {
				// Hashtags der Message in der messagehashtags Tabelle zuordnen
	        	for(Hashtag h : message.getHashtagList()) {
	        		// Wenn das Hashtag bereits der Message zugeordnet wurde 
	        		// soll das nächste hashtag überprüft werden
					if(messageContainsHashtag(message, h)) {
						continue;
					} else {
		        		insertMessageHashtag(message, h);
					}
	  			}
	        }
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return message;
	}

  /**
   * Einfügen eines <code>Message</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt. Es werden ebenfalls mit der Message verbundene Hashtags verlinkt
   * und gegebenenfalls erzeugt.
   * Da es bei einem Post keinen Empfänger gibt wird bewusst auf die Übergabe der
   * ChatID verzichtet.
   * 
   * @param message das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
	public Message insertPost(Message message) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zunächst schauen wir nach, welches der momentan höchste
		     * Primärschlüsselwert ist.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `message`");
			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * message erhält den bisher maximalen, nun um 1 inkrementierten
	         * Primärschlüssel.
	         */
	        message.setId(rs.getInt("maxid") + 1);

	        stmt = con.createStatement();

	        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
	        int result = stmt.executeUpdate("INSERT INTO `message` (`id`, `text`, `autorId`) VALUES (" + message.getId() + ", '" + message.getText() + "', " + message.getUserId() + ")");
	        
	        if (result != 0 && message.getHashtagList() != null) {
				// Hashtags der Message in der messagehashtags Tabelle zuordnen
	        	for(Hashtag h : message.getHashtagList()) {
	        		// Wenn das Hashtag bereits der Message zugeordnet wurde 
	        		// soll das nächste hashtag überprüft werden
					if(messageContainsHashtag(message, h)) {
						continue;
					} else {
		        		insertMessageHashtag(message, h);
					}
	  			}
	        }
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return message;
	}

  /**
   * Methode zum einfügen einer Message-Hashtag Beziehung in die Zwischentabelle
   * "MessageHashtags".
   * 
   * @param message das zu verlinkende Message-Objekt
   * @param hashtag das zu verlinkende Hashtag-Objekt
   * 
   */
	public void insertMessageHashtag(Message message, Hashtag hashtag) {
		
		// Wir werden eine Methode des HashtagMappers benötigen.
		HashtagMapper hashtagmapper = HashtagMapper.hashtagMapper();
		Connection con = DBConnection.connection();
		
		try {
			// Zunächst überprüfen ob der Hashtag existiert.
			if(hashtagmapper.findHashtagByKeyword(hashtag) == null) {
				// Wenn nicht wird dieser zunächst eingefügt.
				hashtag = hashtagmapper.insert(hashtag);
			}
			
			// Erst jetzt können wir erfolgreich mappen.
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("INSERT INTO `messagehashtags`(`messageid`, `hashtagid`) VALUES (" 
							+ message.getId() + "," 
							+ hashtag.getId() + ")");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
  /**
   * Methode zum Überprüfen ob die übergebenen Objekte bereits in einer
   * Beziehung stehen.
   * 
   * @param message das zu verlinkende Message-Objekt
   * @param hashtag das zu verlinkende Hashtag-Objekt
   * @return true Wenn das MessageObjekt bereits mit dem HashtagObjekt erfasst ist
   */
	public boolean messageContainsHashtag(Message message, Hashtag hashtag) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * "
					+ "FROM `dbmessenger`.`messagehashtags` "
					+ "WHERE `messagehashtags`.`messageid` = " + message.getId() 
					+ "AND `messagehashtags`.`hashtagid` = " + hashtag.getId());
			
			if(rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
  /**
   * Wiederholtes Schreiben eines Objekts in die Datenbank. In diesem
   * Fall wird ebenfalls auf verlinkte Hashtags geachtet und ggf. neu
   * aufgesetzt.
   * 
   * @param message das Objekt, das in die DB geschrieben werden soll
   * @return das als Parameter übergebene Objekt
   */
	public Message update(Message message) {
		
		Connection con = DBConnection.connection();

	    try {
	    	Statement stmt = con.createStatement();
			int result = stmt.executeUpdate("UPDATE `dbmessenger`.`message` SET `text`= '" + message.getText() + "' WHERE id=" + message.getId());

			if (result != 0) {
				// Zunächst entfernen wir alle alten Hashtag Vermerke um eine Inkonsistenz
				// mit dem alten Textinhalt zu vermeiden
				deleteAllHashtagsOfMessage(message);
				
				// Da alle Hashtag Vermerke entfernt wurden, können wir auf die
				// Überprüfung vorhandener Einträge verzichten.
				for(Hashtag h : message.getHashtagList()) {
		        	insertMessageHashtag(message, h);
	  			}
			}
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Message message) zu wahren, geben wir message zurück
	    return message;
	}
	
	/**
	 * Löschen der Daten eines <code>Message</code>-Objekts aus der
	 * Datenbank. Etwaige Löschweitergaben aufgrund von Fremdschlüsselbeziehungen
	 * werden über die Einstellungen der Datenbank geregelt.
	 * 
	 * @param messageId die ID der zu löschenden Nachricht
	 */
	public void delete(int messageId) {
		
		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      
	      stmt.executeUpdate("DELETE FROM `dbmessenger`.`message` WHERE `id` = " + messageId);
	      stmt.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}

	/**
	 * Löschen aller Verbindungen zu Hashtags die das übergebene
	 * MessageObjekt hat.
	 * 
	 * @param message die Message deren Verbindungen aufgelöst werden sollen.
	 */
	public void deleteAllHashtagsOfMessage(Message message) {
		Connection con = DBConnection.connection();
		
	    try {
	      Statement stmt = con.createStatement();
	      
	      stmt.executeUpdate("DELETE FROM `dbmessenger`.`messagehashtags` WHERE `messageid` = " + message.getId());
	      stmt.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}

 /**
   * Suchen aller Messages eines bestimmten Nutzers. Es wird eine
   * Liste aller passenden Nachrichten zurückgegeben.
   * 
   * @param user Das UserObjekt nach dessen Nachrichten gesucht wird
   * @return MessageObjekte des gesuchten Nutzers.
   */
	public ArrayList<Message> findAllMessagesOfUser(User user) {
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * "
											+ "FROM `dbmessenger`.`message` "
											+ "WHERE `autorId` = " + user.getId());
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("Id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorID"));
				message.setChatId(rs.getInt("chatID"));
				message.setHashtagList(findAllHashtagsOfMessage(rs.getInt("Id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
 /**
   * Suchen aller Messages ohne Empfänger eines bestimmten Nutzers. Es wird eine
   * Liste aller passenden Nachrichten zurückgegeben.
   * 
   * @param userId Die ID des Nutzers nach dessen Nachrichten gesucht wird
   * @return MessageObjekte des gesuchten Nutzers.
   */
	public ArrayList<Message> findAllPostsOfUser(int userId) {
		
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`message` WHERE `chatId` IS NULL AND `autorId` = " + userId);
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(-1);
				message.setHashtagList(findAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
 /**
   * Suchen aller Hashtags einer bestimmten Nachricht. Es wird eine
   * Liste aller passenden Nachrichten zurückgegeben.
   * 
   * @param messageId Die ID der Message nach deren Hashtags gesucht wird
   * @return HashtagObjekte der gesuchten Message.
   */
	public ArrayList<Hashtag> findAllHashtagsOfMessage(int messageId) {
		
		Connection con = DBConnection.connection();
		ArrayList<Hashtag> result = new ArrayList<Hashtag>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `messagehashtags`.`hashtagId`, `hashtag`.`text`, `hashtag`.`creationDate` "
											+ "FROM `hashtag` INNER JOIN `messagehashtags` "
											+ "ON `hashtag`.`id` = `messagehashtags`.`hashtagId` "
											+ "WHERE `messagehashtags`.`messageId` = " + messageId);
			
			while(rs.next()) {
				Hashtag hashtag = new Hashtag();
				hashtag.setId(rs.getInt("hashtagid"));
				hashtag.setKeyword(rs.getString("text"));
				hashtag.setCreationDate(rs.getDate("creationDate"));
				
				result.add(hashtag);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

 /**
   * Suchen aller Nachrichten ohne Empfänger die einen bestimmten Hashtag enthalten. Es wird eine
   * Liste aller passenden Nachrichten zurückgegeben.
   * 
   * @param hashtagId Die ID des Hashtags nach dem gefiltert wird.
   * @return MessageObjekte des gesuchten Hashtgs.
   */
	public ArrayList<Message> findAllPostsWithHashtag(int hashtagId) {
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `messagehashtags`.`messageId`, `messagehashtags`.`hashtagId`, `message`.`text`, `message`.`autorID`, `message`.`chatId`, `message`.`creationDate` "
											+ "FROM `messagehashtags` INNER JOIN `message` "
											+ "ON `messagehashtags`.`messageId` = `message`.`id` "
											+ "WHERE `message`.`chatId` IS NULL AND `messagehashtags`.`hashtagId` = " + hashtagId);
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("messageId"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorID"));
				message.setChatId(rs.getInt("chatID"));
				message.setHashtagList(findAllHashtagsOfMessage(rs.getInt("messageId")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

 /**
   * Suchen aller Nachrichten eines gegebenen Zeitraums. Es wird eine
   * Liste aller passenden Nachrichten zurückgegeben.
   * 
   * @param start Der Startzeitpunkt als String im Formag "yyyy-MM-dd hh:mm:ss"
   * @param end Der Endzeitpunkt als String im Formag "yyyy-MM-dd hh:mm:ss"
   * @return MessageObjekte des gesuchten Zeitraums.
   */
	public ArrayList<Message> findAllMessagesOfPeriod(String start, String end) {
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * "
											+ "FROM `dbmessenger`.`message` "
											+ "WHERE `creationDate` BETWEEN '" + start + "' AND '" + end + "'");
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("Id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorID"));
				message.setChatId(rs.getInt("chatID"));
				message.setHashtagList(findAllHashtagsOfMessage(rs.getInt("Id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
}
