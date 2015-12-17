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


/**
 * Mapper-Klasse, die <code>Chat</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see ChatMapper
 * @author Thies & Ioannidou
 */
public class ChatMapper {
	
	/**
	 * Die Klasse ChatMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see ChatMapper()
	 */
	private static ChatMapper chatMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected ChatMapper() {
	}
	
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
	   * @see chatMapper()
	   */
	  public static ChatMapper chatMapper() {
	    if (chatMapper == null) {
	    	chatMapper = new ChatMapper();
	    }

	    return chatMapper;
	  }
	  
	  /**
	   * Einfügen eines <code>Chats</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  public Chat insertChat(Chat chat) {
		  Connection con = DBConnection.connection();
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zunächst schauen wir nach, welches der momentan höchste
		       * Primärschlüsselwert ist.
		       */
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(chatId) AS maxid "
		          + "FROM Chat ");

		      if (rs.next()) {
		        /*
		         * u erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		    	  chat.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = chat.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	  chat.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Chat VALUES ('"
		        + chat.getId() +"','"
		        + chat.getCreationDate() +"');");
		        
		      //TODO Eintrag in Zwischentabelle für die Memberlist
		      //TODO Eintrag in Zwischentabelle für die Messagelist
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return chat;
	  }
	  
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   */
	  public Chat updateChat(Chat chat) {
	    Connection con = DBConnection.connection();
	    
	    // Für Statement ID in Integer umwandeln
	    Integer chatId = new Integer(chat.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	      
	    //TODO: Statement anpassen sobald DB steht
	      stmt.executeUpdate("UPDATE Chat SET "
	    		  
	    		//TODO Eintrag in Zwischentabelle für die Memberlist
			      //TODO Eintrag in Zwischentabelle für die Messagelist
	    		  
	      		+"' WHERE chatID='"+chatId.toString()+"';");

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return chat;
	  }
	  
	  /**
	   * Löschen des Chat-Objektes aus der Datenbank 
	   */
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
	  
	  /**
	   * Eine Chat in der Datenbank anhand seiner 
	   * ID finden
	   */
	  public Chat findByID (String id){
			Connection con = DBConnection.connection();
			Chat chat = null;
			try{
				Statement stmt = con.createStatement();
			    
				//TODO: Statement anpassen sobald DB steht
			    ResultSet rs = stmt.executeQuery("SELECT * FROM Chat WHERE chatID ='"
			    		+id+"';");
			    //Da es nur einen Chat mit dieser ID geben kann ist davon auszugehen, dass das ResultSet nur eine Zeile enthält
			    if(rs.next()){
			    	chat = new Chat();
			    	chat.setId(rs.getInt("chatID"));
			    	
			    	//TODO Eintrag in Zwischentabelle für die Memberlist
				      //TODO Eintrag in Zwischentabelle für die chatlist
			    	
			    			    	
			    	// Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
			        // die Datenbank zu speichern 
			        java.sql.Timestamp sqlDate = rs.getTimestamp("creationDate");
		     	 	chat.setCreationDate(sqlDate);
			    }
			}
			catch(SQLException e){
				e.printStackTrace();
			}
			return chat;
		}
	  
	  
	  //TODO Die Methode find by User and Time fehlt noch

}
