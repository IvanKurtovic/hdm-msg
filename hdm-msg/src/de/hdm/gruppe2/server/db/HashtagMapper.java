package de.hdm.gruppe2.server.db;

import java.util.ArrayList;
import java.util.Vector;
import java.sql.*;
import de.hdm.gruppe2.shared.bo.*;

public class HashtagMapper {

	private static HashtagMapper hashtagMapper = null;
	
	protected HashtagMapper() {}
	
	public static HashtagMapper hashtagMapper() {
		if(hashtagMapper == null) {
			hashtagMapper = new HashtagMapper();
		}
		
		return hashtagMapper;
	}
	
	public Hashtag insert(Hashtag hashtag) {
		
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			
			/*
		     * Zunächst schauen wir nach, welches der momentan höchste
		     * Primärschlüsselwert ist.
		     * TODO: Queries definieren sobald die Datenbankstruktur steht.
		     */
			ResultSet rs = stmt.executeQuery("SELECT MAX(`id`) AS maxid FROM `dbmessenger`.`hashtag`");
			// Wenn wir etwas zurückerhalten, kann dies nur einzeilig sein
		      if (rs.next()) {
		        /*
		         * hashtag erhält den bisher maximalen, nun um 1 inkrementierten
		         * Primärschlüssel.
		         */
		    	 hashtag.setId(rs.getInt("maxid") + 1);

		        stmt = con.createStatement();

		        // Jetzt erst erfolgt die tatsächliche Einfügeoperation
		        stmt.executeUpdate("INSERT INTO `dbmessenger`.`hashtag` (`id`, `text`) VALUES (" + hashtag.getId() + ", '" + hashtag.getKeyword() + "')");
		      }
		    }
		    catch (SQLException e) {
		      e.printStackTrace();
		    }
		
		return hashtag;
	}
	
	public Hashtag update(Hashtag hashtag) {

		Connection con = DBConnection.connection();

	    try {
	      Statement stmt = con.createStatement();
	      //TODO Query einfügen sobald die Datenbankstruktur steht.
	      stmt.executeUpdate("");

	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }

	    // Um Analogie zu insert(Hashtag hashtag) zu wahren, geben wir hashtag zurück
	    return hashtag;
	}
	
	public void delete(Hashtag hashtag) {

		Connection con = DBConnection.connection();

		try {
	      Statement stmt = con.createStatement();
	      
	      stmt.executeUpdate("DELETE FROM `dbmessenger`.`hashtag` WHERE `id` = " + hashtag.getId());
	      stmt.close();
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
	}
	
	public Hashtag findById(int hashtagId) {
		Connection con = DBConnection.connection();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag` WHERE `text` = " + hashtagId);
	      
	      if(rs.next()) {
	    	  Hashtag hashtag = new Hashtag();
	    	  
	    	  hashtag.setId(rs.getInt("id"));
	    	  hashtag.setKeyword(rs.getString("text"));
	    	  hashtag.setCreationDate(rs.getDate("creationDate"));
	    	  
	    	  return hashtag;
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	public Hashtag findHashtagByKeyword(Hashtag hashtag) {
		Connection con = DBConnection.connection();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag` WHERE `text` = '" + hashtag.getKeyword() + "'");
	      
	      if(rs.next()) {
	    	  
	    	  hashtag.setId(rs.getInt("id"));
	    	  hashtag.setKeyword(rs.getString("text"));
	    	  hashtag.setCreationDate(rs.getDate("creationDate"));
	    	  
	    	  return hashtag;
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return null;
	}
	
	public ArrayList<Hashtag> getAllHashtags() {
		Connection con = DBConnection.connection();
		ArrayList<Hashtag> allHashtags = new ArrayList<Hashtag>();
		
		try {
	      Statement stmt = con.createStatement();
	      ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtag`");
	      
	      while(rs.next()) {
	    	  Hashtag hashtag = new Hashtag();
	    	  
	    	  hashtag.setId(rs.getInt("id"));
	    	  hashtag.setKeyword(rs.getString("text"));
	    	  hashtag.setCreationDate(rs.getDate("creationDate"));
	    	  
	    	  allHashtags.add(hashtag);
	      }
	    }
	    catch (SQLException e) {
	      e.printStackTrace();
	    }
		return allHashtags;
	}
}
