package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.sql.PreparedStatement;

import de.hdm.gruppe2.shared.bo.*;

/**
 * 
 * 
 * @author Thies
 * 
 * 
 * 
 */
public class UserMapper {

	private static UserMapper userMapper = null;
	
	protected UserMapper() {}
	
	public static UserMapper usermapper() {
		if (userMapper == null) {
			userMapper = new UserMapper();
		}
		return userMapper;
	}

	public User insert(User user) throws IllegalArgumentException {
		// DB-Verbindung herstellen
		Connection con = DBConnection.connection();
		try {
			
			User tmpUser = findUserByEmail(user.getEmail());
			
			if(tmpUser == null) {
				
				// Insert-Statement erzeugen
				Statement stmt = con.createStatement();
				// Zunächst wird geschaut welches der momentan höchste
				// Primärschlüssel ist.
				ResultSet rs = stmt.executeQuery("SELECT MAX(id) AS maxid FROM user");
		
				// Wenn ein Datensatz gefunden wurde, wird auf diesen zugegriffen
				if(rs.next()) {
					// Die gefundene id wird um 1 inkrementiert und in das neue User-Objekt geschrieben.
					user.setId(rs.getInt("maxid") + 1);
				}
				
				String sql = "INSERT INTO `user`(`id`, `email`, `nickname`) "
						+ "VALUES (?, ?, ?)";

				PreparedStatement preStmt;
				preStmt = con.prepareStatement(sql);
				preStmt.setString(1, Integer.toString(user.getId()));
				preStmt.setString(2, user.getEmail());
				preStmt.setString(3, user.getNickname());
				preStmt.executeUpdate();
				preStmt.close();
			} else {
				return tmpUser;
			}
		} catch (SQLException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return user;
	}

	public User update(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement preStmt;
			preStmt = con.prepareStatement("UPDATE `user` SET nickname=?, email=? WHERE id=" + user.getId());
			preStmt.setString(1, user.getNickname());
			preStmt.setString(2, user.getEmail());
			preStmt.executeUpdate();
			preStmt.close();		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return user;
	}

	/**
	 * Lï¿½schen der Daten eines <code>User</code>-Objekts aus der
	 * Datenbank.
	 * 
	 * @param a
	 *
	 *            das aus der DB zu lï¿½schende "Objekt"
	 *            @Autor Thies
	 */
	public void delete(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM `user` WHERE id="	+ user.getId());
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
	}

	/**
	 * Diese Methode ermÃ¶glicht es alle Nutzer aus der Datenbank in einer Liste
	 * zu finden und anzuzeigen.
	 * 
	 * @return allUser
	 *	@Autor Thies
	 *
	 */
	public ArrayList<User> findAllUsers() throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		ArrayList<User> allUsers = new ArrayList<User>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` ORDER BY id");
	
			while (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				allUsers.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return allUsers;
	}
	
	public ArrayList<User> findAllUsersWithoutLoggedInUser(User loggedInUser) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		ArrayList<User> allUsers = new ArrayList<User>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE `id` <> " + loggedInUser.getId());
	
			while (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				allUsers.add(user);
			}
			stmt.close();
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return allUsers;
	}

	/**
	 * Diese Methode ermÃ¶glicht einen Nutzer anhand seines Nachnamens zu finden
	 * und anzuzeigen.
	 * 
	 * @return uebergebener Paramater
	 * @author Thies
	 * @author Serkan
	 */
	public User findUserByNickname(String nickname) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE `nickname`='" + nickname + "'");
	
			if (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));	
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return null;
	}

	/**
	 * Diese Methode ermÃ¶glicht einen Nutzer anhand seiner Email zu finden
	 * und anzuzeigen.
	 * 
	 * @return uebergebener Paramater
	 * @author Thies
	 * @author Serkan
	 * 
	 */
	public User findUserByEmail(String email) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE email='"	+ email + "'");
	
			if (rs.next()) {
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
	
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return null;
	}
	
	/**
	 * Diese Methode ermÃ¶glicht einen Nutzer anhand des Primï¿½rschlï¿½ssels zu
	 * finden und anzuzeigen.
	 * 
	 * @return uebergebener Paramater
	 * @author Thies
	 * @author Serkan 
	 * 
	 */
	public User findUserById(int id) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery("SELECT * FROM `user` WHERE id=" + id);
			
			if(rs.next()){
				User user = new User();
				
				user.setId(rs.getInt("id"));
				user.setNickname(rs.getString("nickname"));
				user.setEmail(rs.getString("email"));
				user.setCreationDate(rs.getDate("creationDate"));
				
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Error with Database or Connection failed"
					+ e.toString());
		}
		return null;
	}
}


