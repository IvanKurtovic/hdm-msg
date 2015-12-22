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

	
	protected UserMapper() {

	}

	
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
			// Insert-Statement erzeugen
			// Statement stmt = con.createStatement();
			// Zun�chst wird geschaut welches der momentan h�chste
			// Prim�rschl�ssel ist
			// ResultSet
			// rs=stmt.executeQuery("SELECT MAX(UserID) AS maxid "+"FROM user");

			// Wenn ein Datensatz gefunden wurde, wird auf diesen zugegriffen
			// if(rs.next()) @Author: Thies{
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date d = new Date();
			try {
				d = sdf.parse("21/12/2015");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//user.setCreationDate(creationDate);

			String sql = "INSERT INTO `user`(`GoogleID`, `vorname`, `nachname`, `email`, `nickname`, `datum`) "
					+ "VALUES (NULL, ?, ?, ?, ?, ?)";

			PreparedStatement preStmt;
			preStmt = con.prepareStatement(sql);
			preStmt.setString(1, user.getFirstName());
			preStmt.setString(3, user.getLastName());
			preStmt.setString(2, user.getEmail());
			preStmt.setString(4, user.getGoogleId());
			preStmt.executeUpdate();
			preStmt.close();

		
	

			
		} catch (SQLException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("Datenbank fehler!"
					+ e.toString());
		}
		return user;
	}

	public User update(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			PreparedStatement preStmt;
			preStmt = con
					.prepareStatement("UPDATE nutzer SET firstName=?, "
							+ "LastName=?, email=?, date=? WHERE GoogleID="
							+ user.getGoogleId());
			preStmt.setString(1, user.getFirstName());
			preStmt.setString(2, user.getLastName());
			preStmt.setString(3, user.getEmail());
			preStmt.setString(5, user.getCreationDate().toString());
			preStmt.executeUpdate();
			preStmt.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Datenbank fehler!"
					+ e.toString());
		}
		return user;
	}

	/**
	 * L�schen der Daten eines <code>User</code>-Objekts aus der
	 * Datenbank.
	 * 
	 * @param a
	 *
	 *            das aus der DB zu l�schende "Objekt"
	 *            @Autor Thies
	 */
	public void delete(User user) throws IllegalArgumentException {
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate("DELETE FROM user WHERE userID="
					+ user.getId());
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Datenbank fehler!"
					+ e.toString());
		}
	}




/**
 * Diese Methode ermöglicht es alle Nutzer aus der Datenbank in einer Liste
 * zu finden und anzuzeigen.
 * 
 * @return allUser
 *	@Autor Thies
 *
 */
public ArrayList<User> findAllUser() throws IllegalArgumentException {
	Connection con = DBConnection.connection();
	ArrayList<User> allUser = new ArrayList<User>();
	try {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt
				.executeQuery("SELECT userID, firstName, LastName, email, date "
						+ "FROM user ORDER BY userID");

		while (rs.next()) {
			User user = new User();
			user.setId(rs.getInt("userID"));
			user.setFirstName(rs.getString("firstName"));
			user.setLastName(rs.getString("lastName"));
			user.setEmail(rs.getString("email"));
			//user.setCreationDate(rs.getString("date"));

			allUser.add(user);
		}
		stmt.close();
		rs.close();
		
	} catch (SQLException e) {
		e.printStackTrace();
		throw new IllegalArgumentException("Datenbank fehler!"
				+ e.toString());
	}
	return allUser;
}

}