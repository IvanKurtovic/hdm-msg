package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Chat;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>Chat</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see UserMapper, HashtagMapper, MessageMapper, HashtagSubscriptionMapper, UserSubscriptionMapper
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */
public class ChatMapper {

  /**
   * Die Klasse ChatMapper wird nur einmal instantiiert. Man spricht hierbei
   * von einem sogenannten <b>Singleton</b>.
   * <p>
   * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
   * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
   * einzige Instanz dieser Klasse.
   * 
   * @see chatMapper()
   */
	private static ChatMapper chatMapper = null;

  /**
   * Geschützter Konstruktor - verhindert die Möglichkeit, mit <code>new</code>
   * neue Instanzen dieser Klasse zu erzeugen.
   */
	protected ChatMapper(){}

  /**
   * Diese statische Methode kann aufgrufen werden durch
   * <code>ChatMapper.chatMapper()</code>. Sie stellt die
   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
   * Instanz von <code>ChatMapper</code> existiert.
   * <p>
   * 
   * <b>Fazit:</b> ChatMapper sollte nicht mittels <code>new</code>
   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
   * 
   * @return DAS <code>ChatMapper</code>-Objekt.
   * @see chatMapper
   */
	public static ChatMapper chatMapper(){
		if (chatMapper == null) {
			chatMapper = new ChatMapper();
		}
		return chatMapper;
	}

  /**
   * Einfügen eines <code>Chat</code>-Objekts in die Datenbank. Dabei wird
   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
   * berichtigt. Die Verlinkung der Chat-Teilnehmer auf die Zwischentabelle
   * ChatParticipants erfolgt ebenfalls hier, durch Aufruf der Methode insertChatParticipant. 
   * 
   * @param chat das zu speichernde Objekt
   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
   *         <code>id</code>.
   */
	public Chat insert(Chat chat) {
		Connection con = DBConnection.connection();
	  
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `chat` ");
  
  		  	if (rs.next()) {

  		  		chat.setId(rs.getInt("maxid") + 1);
  		  		
  		  		stmt = con.createStatement();
  		  		int result = stmt.executeUpdate("INSERT INTO `chat`(`id`, `name`) VALUES (" 
  		  							+ chat.getId() + ",'" 
  		  							+ chat.getName() + "')");
  		  		
  		  		if(result != 0) {
  		  			// Chat Teilnehmer der Teilnehmertabelle zuweisen.
  		  			for(User u : chat.getMemberList()) {
  		  				insertChatParticipant(u, chat);
  		  			}
  		  		}
  		    } 
		} catch (SQLException e2) {
  		      e2.printStackTrace();
  		}
  		return chat;
	}

  /**
   * Verlinken eines Chat-Teilnehmers mit einem Chat auf der Zwischentabelle
   * ChatParticipants. 
   * 
   * @param participant der Teilnehmer des Chats
   * @param chat der Chat mit dem verlinkt werden soll
   * 
   */
	public void insertChatParticipant(User participant, Chat chat) {
		Connection con = DBConnection.connection();
		  
		try {
			Statement stmt = con.createStatement();
  		  	
  		  	stmt.executeUpdate("INSERT INTO `chatparticipants`(`chatid`, `userid`) VALUES (" 
  		  							+ chat.getId() + "," 
  		  							+ participant.getId() + ")");
  		     
		} catch (SQLException e2) {
  		      e2.printStackTrace();
  		}
	}

	/**
	 * Löschen der Daten eines <code>Chat</code>-Objekts aus der
	 * Datenbank. Etwaige Löschweitergaben aufgrund von Fremdschlüsselbeziehungen
	 * werden über die Einstellungen der Datenbank geregelt.
	 * 
	 * @param chat das zu löschende ChatObjekt
	 */
	public void delete(Chat chat) {
		
		Connection con = DBConnection.connection();
				 
 	    try {
 	    	
 	      Statement stmt = con.createStatement();
 	      stmt.executeUpdate("DELETE FROM `chat` WHERE id =" + chat.getId());
 	      
 	    } catch (SQLException e2) {
 	      e2.printStackTrace();
 	    }
	}

	/**
	 * Löschen einer Verbindungen zwischen einem Chat und einem Teilnehmer.
	 * Durch diese Methode kann ein Nutzer einen Chat verlassen ohne das dieser
	 * gelöscht wird.
	 * 
	 * @param participant der Teilnehmer des Chats
	 * @param chat der Chat in dem der Teilnehmer ist
	 */
	public void deleteChatParticipant(Chat chat, User participant) {
		
		Connection con = DBConnection.connection();
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `chatparticipants` WHERE chatId=" + chat.getId() + " AND userId=" + participant.getId());
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

 /**
   * Suchen aller Chats der Datenbank. Es wird eine
   * Liste aller passenden Chats zurückgegeben.
   * 
   * @return ChatsObjekte der Datenbank
   */
	public ArrayList<Chat> findAllChats() {
		
		Connection con = DBConnection.connection();
		ArrayList<Chat> allChats = new ArrayList<Chat>();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `chat`");
			
			while(rs.next()) {
				Chat chat = new Chat();
				chat.setId(rs.getInt("id"));
				chat.setName(rs.getString("name"));
				chat.setCreationDate(rs.getDate("creationDate"));
				chat.setMemberList(findAllParticipantsOfChat(chat));
				chat.setMessageList(getAllMessagesOfChat(chat));
				
				allChats.add(chat);
			}
			
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allChats;
	}
	
 /**
   * Suchen aller Chats eines bestimmten Nutzers. Es wird eine
   * Liste aller passenden Chats zurückgegeben.
   * 
   * @param user Der Nutzer nach dessen Chats gesucht wird
   * @return ChatsObjekte des Nutzers
   */	
	public ArrayList<Chat> findAllChatsOfUser(User user) {
		
		Connection con = DBConnection.connection();
		ArrayList<Chat> allChats = new ArrayList<Chat>();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `chatparticipants`.`chatId`, `chat`.`name`, `chat`.`creationDate` "
											+ "FROM `chatparticipants` INNER JOIN `chat` "
											+ "ON `chatparticipants`.`chatId` = `chat`.`id` "
											+ "WHERE `userId` = " + user.getId());
			
			while(rs.next()) {
				Chat chat = new Chat();
				chat.setId(rs.getInt("chatId"));
				chat.setName(rs.getString("name"));
				chat.setCreationDate(rs.getDate("creationDate"));
				chat.setMemberList(findAllParticipantsOfChat(chat));
				chat.setMessageList(getAllMessagesOfChat(chat));
				
				allChats.add(chat);
			}
			
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allChats;
	}

 /**
   * Suchen aller Teilnehmer eines bestimmten Chats. Es wird eine
   * Liste aller passenden Teilnehmer zurückgegeben.
   * 
   * @param chat Der Chat nach dessen Teilnehmer gesucht werden
   * @return UserObjekte des Chats
   */	
	public ArrayList<User> findAllParticipantsOfChat(Chat chat) {
		
		Connection con = DBConnection.connection();
		ArrayList<User> participantsOfChat = new ArrayList<User>();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `chatparticipants`.`userid`, `chatparticipants`.`chatId`, `user`.`email`, `user`.`nickname`, `user`.`creationDate` " 
											+ "FROM `chatparticipants` INNER JOIN `user` "
											+ "ON `chatparticipants`.`userId` = `user`.`id` "
											+ "WHERE `chatparticipants`.`chatId` = " + chat.getId());
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("userId"));
				user.setEmail(rs.getString("email"));
				user.setNickname(rs.getString("nickname"));
				user.setCreationDate(rs.getDate("creationDate"));
				
				participantsOfChat.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return participantsOfChat;
	}

 /**
   * Suchen aller Messages eines bestimmten Chats. Es wird eine
   * Liste aller passenden Messages zurückgegeben.
   * 
   * @param chat Der Chat nach dessen Messages gesucht wird
   * @return MessageObjekte des Chats
   */
	public ArrayList<Message> getAllMessagesOfChat(Chat chat) {
		
		Connection con = DBConnection.connection();
		ArrayList<Message> messagesOfChat = new ArrayList<Message>();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `message` WHERE chatId=" + chat.getId());
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(rs.getInt("chatId"));
				message.setCreationDate(rs.getDate("creationDate"));
				
				messagesOfChat.add(message);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messagesOfChat;
	}
}

