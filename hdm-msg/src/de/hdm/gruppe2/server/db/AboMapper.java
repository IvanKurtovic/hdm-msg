package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import de.hdm.gruppe2.shared.bo.Abo;

/**
 * Mapper-Klasse, die <code>Abo</code>-Objekte auf eine relationale
 * Datenbank abbildet. Hierzu wird eine Reihe von Methoden zur Verfügung
 * gestellt, mit deren Hilfe z.B. Objekte gesucht, erzeugt, modifiziert und
 * gelöscht werden können. Das Mapping ist bidirektional. D.h., Objekte können
 * in DB-Strukturen und DB-Strukturen in Objekte umgewandelt werden.
 * 
 * @see AboMapper
 * @author Thies & Ioannidou
 */
public class AboMapper {
	
	/**
	 * Die Klasse AboMapper wird nur einmal instantiiert. Man spricht
	 * hierbei von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal
	 * für sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie
	 * speichert die einzige Instanz dieser Klasse.
	 * 
	 * @see AboMapper()
	 */
	private static AboMapper aboMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit
	 * <code>new</code> neue Instanzen dieser Klasse zu erzeugen.
	 */
	protected AboMapper() {
	}
	
	/**
	   * Diese statische Methode kann aufgrufen werden durch
	   * <code>AboMapper.aboMapper()</code>. Sie stellt die
	   * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	   * Instanz von <code>AboMapper</code> existiert.
	   * <p>
	   * 
	   * <b>Fazit:</b> AboMapper sollte nicht mittels <code>new</code>
	   * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	   * 
	   * @return DAS <code>AboMapper</code>-Objekt.
	   * @see aboMapper()
	   */
	  public static AboMapper aboMapper() {
	    if (aboMapper == null) {
	    	aboMapper = new AboMapper();
	    }

	    return aboMapper;
	  }
	  
	  /**
	   * Einfügen eines <code>Abo</code>-Objekts in die Datenbank. Dabei wird
	   * auch der Primärschlüssel des übergebenen Objekts geprüft und ggf.
	   * berichtigt.
	   * @param a das zu speichernde Objekt
	   * @return das bereits übergebene Objekt, jedoch mit ggf. korrigierter
	   * <code>id</code>.
	   */
	  
	  
	  public Abo insertAbo(Abo abo) {
		  Connection con = DBConnection.connection();
		  
		  // TODO IF Schleife einbauen, die Nutzer & Hashtags Abo verwaltet
		  
		  try {
		      Statement stmt = con.createStatement();

		      /*
		       * Zunächst schauen wir nach, welches der momentan höchste
		       * Primärschlüsselwert ist.
		       */
		      //TODO: Statement anpassen sobald DB steht
		      ResultSet rs = stmt.executeQuery("SELECT MAX(aboID) AS maxid "
		          + "FROM Abo ");

		      if (rs.next()) {
		        /*
		         * Man erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		    	  abo.setId(rs.getInt("maxid") + 1);
	    	 
		    	  
		    	  // Java Util Date wird umgewandelt in SQL Date um das Änderungsdatum in
		    	  // die Datenbank zu speichern 
		     	  Date utilDate = abo.getCreationDate();
		     	  java.sql.Timestamp sqlDate = new java.sql.Timestamp(utilDate.getTime());  
		     	  DateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		     	  df.format(sqlDate);
		     	  
		     	 abo.setCreationDate(sqlDate);
		     	  
	    	  /*
		       * Das Objekt wird nun in die Datenbank geschrieben 
		       */
		       
		     	  stmt = con.createStatement();
		        
		      //TODO: Statement anpassen sobald DB steht
		        stmt.executeUpdate("INSERT INTO Abo VALUES ('"
		        + abo.getId() +"','"
		        + abo.getCreationDate() +"');");
		        
		      //TODO Eintrag in Zwischentabelle für die Hashtags
		      //TODO Eintrag in Zwischentabelle für die User
		      }
		      
		    }
		  catch (SQLException e2) {
		      e2.printStackTrace();
		    }

		  return abo;
	  }
	  
	  /**
	   * Wiederholtes Schreiben eines Objekts in die Datenbank.
	   */
	  public Abo updateAbo(Abo abo) {
	    Connection con = DBConnection.connection();
	    
	    // Für Statement ID in Integer umwandeln
	    Integer aboId = new Integer(abo.getId());

	    try {
	      Statement stmt = con.createStatement();
	      
	      
	      //TODO Eintrag in Zwischentabelle für die Hashtags
	      //TODO Eintrag in Zwischentabelle für die Sender

	    }
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }

	    return abo;
	  }
	  
	  
	  /**
	   * Löschen des Abo-Objektes aus der Datenbank 
	   */
	  public void deleteAbo(Abo abo) {
	    Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      stmt.executeUpdate("DELETE FROM Abo WHERE aboID ='"+ abo.getId()+"'");
	    }
	    
	    catch (SQLException e2) {
	      e2.printStackTrace();
	    }
	  }
	  
	  
	  //TODO getAboByHashtag Methode
	  //TODO getAboByUser Methode

}
