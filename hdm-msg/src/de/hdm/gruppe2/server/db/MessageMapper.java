package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Mapper-Klasse, die <code>Message</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see MessageMapper
 * @author Thies & Ioannidou
 */
public class MessageMapper {
	
	/**
	 * Die Klasse MessageMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see MessageMapper()
	 */
	private static MessageMapper messageMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected MessageMapper() {
	}
	
	/**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>MessageMapper.messageMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	   * Instanz von <code>MessageMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> MessageMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>MessageMapper</code>-Objekt.
	   * @see messageMapper()
	   */
	  public static MessageMapper messageMapper() {
	    if (messageMapper == null) {
	    	messageMapper = new MessageMapper();
	    }

	    return messageMapper;
	  }
	  
	  /**
	   * Einfügen eines <code>Message</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  
	  
	  public Message insertMessage(Message message) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zunächst schauen wir nach, welches der momentan höchste
		       * Primärschlüsselwert ist.
		       */
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(messageID) AS maxid "
		          + "FROM Message ");

		      if (rs.next()) {
		        /*
		         * Man erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		    	  message.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = message.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	  message.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Message VALUES ('"
		        + message.getId() +"','"
		        + message.getText() +"','"
		        + message.getCreationDate() +"');");
		        
		      //TODO Eintrag in Zwischentabelle für die Hashtags
		      //TODO Eintrag in Zwischentabelle für die Sender
		      //TODO Eintrag in Zwischentabelle für die Chat
		        
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return message;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   */
	  public Message updateMessage(Message message) {
	    Connection con = DBConnection.connection();
	    
	    // Für Statement ID in Integer umwandeln
	    Integer messageId = new Integer(message.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	      
	    //TODO: Statement anpassen sobald DB steht
	      stmt.executeUpdate("UPDATE Message SET "
	      		+"text='"+ message.getText() 
	      		+"' WHERE messageID='"+messageId.toString()+"';");
	      
	      //TODO Eintrag in Zwischentabelle für die Hashtags
	      //TODO Eintrag in Zwischentabelle für die Sender
	      //TODO Eintrag in Zwischentabelle für die Chat

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return message;
	  }
	  
	  
	  /**
	   * Löschen des Message-Objektes aus der Datenbank 
	   */
	  public void deleteMessage(Message message) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM Message WHERE messageID ='"+ message.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  /**
	   * Eine Message in der Datenbank anhand seiner 
	   * ID finden
	   */
	  public Message findByID (int id){
			Connection con = DBConnection.connection();
			Message message = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM Message WHERE messageID ='"
			    		+id+"';");
			    //Da es nur einen Message mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enthält
			    if(rs.next()){
			    	message = new Message();
			    	message.setId(rs.getInt("messageID"));
			    	message.setText(rs.getString("text"));
			    	
			    	//TODO Eintrag aus Zwischentabelle für die Hashtags
				    //TODO Eintrag aus Zwischentabelle für die Sender
				    //TODO Eintrag aus Zwischentabelle für die Chat
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		     	 	message.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return message;
		}
	  
	  /**
	   * Eine Message in der Datenbank anhand eines 
	   * Users und einer bestimmten Zeit finden 
	   */
	  public Message findByUserAndTime(User user, Timestamp startTime, Timestamp endTime){
			Connection con = DBConnection.connection();
			Message message = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM Message WHERE messageID ='"
			    		+user+"';");
			   
			    //Da es nur einen Message mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enthält
			    if(rs.next()){
			    	message = new Message();
			    	message.setId(rs.getInt("messageID"));
			    	message.setText(rs.getString("text"));
			    	
			    	//TODO Eintrag aus Zwischentabelle für die Hashtags
				    //TODO Eintrag aus Zwischentabelle für die Sender
				    //TODO Eintrag aus Zwischentabelle für die Chat
			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		     	 	message.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return message;
		}	  

}
