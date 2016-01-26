package de.hdm.gruppe2.server.db;

import java.util.ArrayList;
import java.util.Vector;
import java.sql.*;
import de.hdm.gruppe2.shared.bo.*;

public class MessageMapper {

	private static MessageMapper messageMapper = null;
	
	protected MessageMapper() {}
	
	public static MessageMapper messageMapper() {
		if(messageMapper == null) {
			messageMapper = new MessageMapper();
		}
		
		return messageMapper;
	}
	
	public Message insertPost(Message message) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zun�chst schauen wir nach, welches der momentan h�chste
		     * Prim�rschl�sselwert ist.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `message`");
			// Wenn wir etwas zur�ckerhalten, kann dies nur einzeilig sein
	      if (rs.next()) {
	        /*
	         * message erh�lt den bisher maximalen, nun um 1 inkrementierten
	         * Prim�rschl�ssel.
	         */
	        message.setId(rs.getInt("maxid") + 1);

	        stmt = con.createStatement();

	        // Jetzt erst erfolgt die tats�chliche Einf�geoperation
	        int result = stmt.executeUpdate("INSERT INTO `message` (`id`, `text`, `autorId`) VALUES (" + message.getId() + ", '" + message.getText() + "', " + message.getUserId() + ")");
	        
	        if (result != 0 && message.getHashtagList() != null) {
				// Hashtags der Message in der messagehashtags Tabelle zuordnen
	        	for(Hashtag h : message.getHashtagList()) {
	        		// Wenn das Hashtag bereits der Message zugeordnet wurde 
	        		// soll das n�chste hashtag �berpr�ft werden
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
	
	public void insertMessageHashtag(Message message, Hashtag hashtag) {
		
		// Wir werden eine Methode des HashtagMappers ben�tigen.
		HashtagMapper hashtagmapper = HashtagMapper.hashtagMapper();
		Connection con = DBConnection.connection();
		
		try {
			// Zun�chst �berpr�fen ob der Hashtag existiert.
			if(hashtagmapper.findHashtagByKeyword(hashtag) == null) {
				// Wenn nicht wird dieser zun�chst eingef�gt.
				hashtagmapper.insert(hashtag);
			}
			
			// Erst jetzt k�nnen wir erfolgreich mappen.
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("INSERT INTO `messagehashtags`(`messageid`, `hashtagid`) VALUES (" 
							+ message.getId() + "," 
							+ hashtag.getId() + ")");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean messageContainsHashtag(Message message, Hashtag hashtag) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`messagehashtags` WHERE `messageid` = " + message.getId() + "AND `hashtagId` = " + hashtag.getId());
			
			if(rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Message update(Message message) {
		
		Connection con = DBConnection.connection();

	    try {
	    	Statement stmt = con.createStatement();
			int result = stmt.executeUpdate("UPDATE `dbmessenger`.`message` SET `text`= '" + message.getText() + "' WHERE id=" + message.getId());

			if (result != 0) {
				// Zun�chst entfernen wir alle alten Hashtag Vermerke um eine Inkonsistenz
				// mit dem alten Textinhalt zu vermeiden
				deleteAllHashtagsOfMessage(message);
				
				// Da alle Hashtag Vermerke entfernt wurden, k�nnen wir auf die
				// �berpr�fung vorhandener Eintr�ge verzichten.
				for(Hashtag h : message.getHashtagList()) {
		        	insertMessageHashtag(message, h);
	  			}
			}
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Message message) zu wahren, geben wir message zur�ck
	    return message;
	}
	
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
	
	public ArrayList<Message> findByAuthor(User user) {
		Connection con = DBConnection.connection();
		ArrayList<Message> result = new ArrayList<Message>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`message` WHERE `chatId` IS NOT NULL AND `autorId` = " + user.getId());
			
			while(rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(rs.getInt("chatId"));
				message.setHashtagList(getAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Message findMessageById(int messageId) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`message` WHERE `id` = " + messageId);
			
			if(rs.next()) {
				Message message = new Message();
				
				message.setId(rs.getInt("id"));
				message.setText(rs.getString("text"));
				message.setUserId(rs.getInt("autorId"));
				message.setChatId(rs.getInt("chatId"));
				message.setHashtagList(getAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				return message;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Vector<Message> findByChat(int chatId) {
		// TODO Abfrage implementieren sobald die Datenbankstruktur steht.
		return null;
	}
	
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
				message.setHashtagList(getAllHashtagsOfMessage(rs.getInt("id")));
				message.setCreationDate(rs.getDate("creationDate"));
				
				result.add(message);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Hashtag> getAllHashtagsOfMessage(int messageId) {
		
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
}
