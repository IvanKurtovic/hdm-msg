package de.hdm.gruppe2.server.db;

import java.sql.Connection;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.Statement;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

import java.util.Date;

import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Chat;

import de.hdm.gruppe2.shared.bo.Message;

import de.hdm.gruppe2.shared.bo.User;

public class ChatMapper {
	
	private static ChatMapper chatMapper = null;
	
	protected ChatMapper(){
		
	}

	public static ChatMapper chatMapper(){
		if (chatMapper == null) {
			chatMapper = new ChatMapper();
		}
		return chatMapper;
	}
	
	 public Chat insertChat(Chat chat) {
		  		  Connection con = DBConnection.connection();
		  		  
		  		  try {
		  		      Statement stmt = con.createStatement();

		  		      ResultSet rs = stmt.executeQuery("SELECT MAX(chatId) AS maxid "
		  		          + "FROM Chat ");
		  
		  		      if (rs.next()) {

		  		    	  chat.setId(rs.getInt("maxid") + 1);

		  		     	  Date utilDate = chat.getCreationDate();
		  		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		  		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		  		     	  df.format(sqlDate);
		  		     	  
		  		     	  chat.setCreationDate(sqlDate);

		  		     	  stmt = con.createStatement();

		  		        stmt.executeUpdate("INSERT INTO Chat VALUES ('"
		  		        + chat.getId() +"','"
		  		        + chat.getCreationDate() +"');");
		  		      }
		  		      
		  		    }
		  		  catch (SQLException e2) {
		  		      e2.printStackTrace();
		  		    }
		  
		  		  return chat;
		  	  }
		  	 
	  
	public Chat updateChat(Chat chat) {
				 	    Connection con = DBConnection.connection();
				 	    
				 	    Integer chatId = new Integer(chat.getId());
				 
				 	    try {
				 	      Statement stmt = con.createStatement();
				 	      
				 	      stmt.executeUpdate("UPDATE Chat SET " +"' WHERE chatID='"+chatId.toString()+"';");
				 
				 	    }
				 	    catch (SQLException e2) {
				 	      e2.printStackTrace();
					    }
				 
				 	    return chat;
				 	  }
	
	public void deleteChat(Chat chat) {
					     Connection con = DBConnection.connection();
					 
					 	    try {
					 	      Statement stmt = con.createStatement();
					 	      stmt.executeUpdate("DELETE FROM Chat WHERE chatID ='"+ chat.getId()+"'");
					 	    }
					 	    
					 	    catch (SQLException e2) {
					 	      e2.printStackTrace();
					 	    }
					 	  }
	
	public Chat findByID (String id){
		 			
		Connection con = DBConnection.connection();
		Chat chat = null;
		 			
		 			try{
		 				Statement stmt = con.createStatement();
		 			    
		 			    ResultSet rs = stmt.executeQuery("SELECT * FROM Chat WHERE chatID ='"+id+"' ORDER BY ID;");;
		 			  
		 			    if(rs.next()){
		 			    	chat = new Chat();
		 			    	chat.setId(rs.getInt("chatID"));
		 			    	   	
		 			    	
		 			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		 		     	 	chat.setCreationDate(sqlDate);
		 			    }
		 			}
		 			catch(SQLException e){
		 				e.printStackTrace();
		 			}
		 			return chat;
		 		}
	public Chat findByUserAndTime(String id){
		
		Connection con = DBConnection.connection();
		Chat chat = null;
		 			
		 			try{
		 				Statement stmt = con.createStatement();
		 			    
		 		
		 			    String date = null;
						ResultSet rs = stmt.executeQuery("SELECT * FROM Chat WHERE userID ='"+id+"' AND Where date = '"+date+"' ;");
		 			  
		 			    if(rs.next()){
		 			    	chat = new Chat();
		 			    	chat.setId(rs.getInt("chatID"));
		 			    	//chat.setDate(rs.getDate("dd/MM/YYYY HH:mm:ss"));   	
		 			    	
		 			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		 		     	 	chat.setCreationDate(sqlDate);
		 			    }
		 			}
		 			catch(SQLException e){
		 				e.printStackTrace();
		 			}
		 			return chat;
		 		}	 	  
	
		 }
	

