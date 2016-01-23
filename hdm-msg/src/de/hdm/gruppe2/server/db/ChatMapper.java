package de.hdm.gruppe2.server.db;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.text.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Chat;

import de.hdm.gruppe2.shared.bo.Message;

import de.hdm.gruppe2.shared.bo.User;

public class ChatMapper {
	
	private static ChatMapper chatMapper = null;
	
	protected ChatMapper(){}

	public static ChatMapper chatMapper(){
		if (chatMapper == null) {
			chatMapper = new ChatMapper();
		}
		return chatMapper;
	}
	
	public Chat insert(Chat chat) {
		Connection con = DBConnection.connection();
	  
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `chat` ");
  
  		  	if (rs.next()) {

  		  		int isPrivate = 0;
  		  		chat.setId(rs.getInt("maxid") + 1);
  		  		
  		  		if(chat.isPrivate()) {
  		  			isPrivate = 1;
  		  		}
  		  		
  		  		stmt = con.createStatement();
  		  		int result = stmt.executeUpdate("INSERT INTO `chat`(`id`, `name`) VALUES (" 
  		  							+ chat.getId() + ",'" 
  		  							+ chat.getName() + "' "
  		  							+ isPrivate + ")");
  		  		
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
	
	public void insertChatParticipant(User participant, Chat chat) {
		Connection con = DBConnection.connection();
		  
		try {
			Statement stmt = con.createStatement();
  		  	stmt = con.createStatement();
  		  	
  		  	stmt.executeUpdate("INSERT INTO `chatparticipants`(`chatid`, `userid`) VALUES (" 
  		  							+ chat.getId() + "," 
  		  							+ participant.getId() + ")");
  		     
		} catch (SQLException e2) {
  		      e2.printStackTrace();
  		}
	}
	  	 
  
	public Chat update(Chat chat) {
		
 	    Connection con = DBConnection.connection();

 	    try {
 	      Statement stmt = con.createStatement();
 	      stmt.executeUpdate("UPDATE `chat` SET name='" + chat.getName() + "' WHERE id=" + chat.getId());
 	    }
 	    catch (SQLException e2) {
 	      e2.printStackTrace();
	    }
 
 	    return chat;
	}

	public void delete(Chat chat) {
		
		Connection con = DBConnection.connection();
				 
 	    try {
 	    	
 	      Statement stmt = con.createStatement();
 	      stmt.executeUpdate("DELETE FROM `chat` WHERE id =" + chat.getId());
 	      
 	    } catch (SQLException e2) {
 	      e2.printStackTrace();
 	    }
	}
	
	public void deleteChatParticipant(Chat chat, User participant) {
		
		Connection con = DBConnection.connection();
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `chatparticipants` WHERE chatId=" + chat.getId() + " AND userId=" + participant.getId());
			
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}

	public Chat findByID (int id) {
		 			
		Connection con = DBConnection.connection();
		Chat chat = new Chat();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `chat` WHERE id="+ id);
		  
			if(rs.next()){
				chat.setId(rs.getInt("id"));
				chat.setName(rs.getString("name"));
			   	chat.setCreationDate(rs.getDate("creationDate"));
		    }

			stmt.close();
			rs.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}

		return chat;
	}
	
	public ArrayList<Chat> getAllChats() {
		
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
				chat.setMemberList(getAllParticipantsOfChat(chat));
				chat.setMessageList(getAllMessagesOfChat(chat));

				if(rs.getInt("isPrivat") == 1) {
					chat.setPrivate(true);
				} else {
					chat.setPrivate(false);
				}
				
				allChats.add(chat);
			}
			
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allChats;
	}
	
	public ArrayList<Chat> getAllPublicChatsOfUser(int userId) {
		
		Connection con = DBConnection.connection();
		ArrayList<Chat> allChats = new ArrayList<Chat>();
		
		try {
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `chatparticipants`.`chatId`, `chat`.`name`, `chat`.`isPrivat`, `chat`.`creationDate` " +
											"FROM `chatparticipants` INNER JOIN `chat` " +
											"ON `chatparticipants`.`chatId` = `chat`.`id` " +
											"WHERE `userId` = " + userId  + " AND `isPrivat` <> 1");
			
			while(rs.next()) {
				Chat chat = new Chat();
				chat.setId(rs.getInt("chatId"));
				chat.setName(rs.getString("name"));
				chat.setCreationDate(rs.getDate("creationDate"));
				chat.setMemberList(getAllParticipantsOfChat(chat));
				chat.setMessageList(getAllMessagesOfChat(chat));
				
				if(rs.getInt("isPrivat") == 1) {
					chat.setPrivate(true);
				} else {
					chat.setPrivate(false);
				}

				allChats.add(chat);
			}
			
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return allChats;
	}
	
	public ArrayList<User> getAllParticipantsOfChat(Chat chat) {
		
		Connection con = DBConnection.connection();
		ArrayList<User> participantsOfChat = new ArrayList<User>();
		
		try {
			
			Statement stmt = con.createStatement();
			// TODO BESSERE QUERY ENTWICKELN --> VON ID ZUM USER OBJEKT!
			ResultSet rs = stmt.executeQuery("SELECT `userId` FROM `chatparticipants` WHERE chatId=" + chat.getId());
			
			while(rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				
				participantsOfChat.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return participantsOfChat;
	}

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

