package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.User;

public class HashtagSubscriptionMapper {

	private static HashtagSubscriptionMapper hashtagSubscriptionMapper = null;
	
	protected HashtagSubscriptionMapper() {}
	
	public static HashtagSubscriptionMapper hashtagSubscriptionMapper() {
		if(hashtagSubscriptionMapper == null) {
			hashtagSubscriptionMapper = new HashtagSubscriptionMapper();
		}
		
		return hashtagSubscriptionMapper;
	}
	
	public void insert(Hashtag hashtag, User user) {
		Connection con = DBConnection.connection();
		
		try {
			if(findByHashtagAndUser(hashtag.getId(), user.getId()) == null) {

				Statement stmt = con.createStatement();			
				stmt.executeUpdate("INSERT INTO `dbmessenger`.`hashtagsubscription`(`hashtagId`, `userId`) VALUES (" + hashtag.getId() + ", " + user.getId() + " )");					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete(int hashtagId, int userId) {
		Connection con = DBConnection.connection();
		
		try {			
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("DELETE FROM `dbmessenger`.`hashtagsubscription` WHERE `hashtagId` = " 
			+ hashtagId + " AND `userId` = " 
			+ userId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public HashtagSubscription findByHashtagAndUser(int hashtagId, int userId) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtagsubscription` WHERE `hashtagId` = " + hashtagId + " AND `userId` = " + userId);
			
			if(rs.next()) {
				HashtagSubscription hs = new HashtagSubscription();
				hs.setRecipientId(rs.getInt("userId"));
				hs.setHashtagId(rs.getInt("hashtagId"));		
				return hs;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<HashtagSubscription> findAllHashtagSubscriptionsOfUser(User user) {
		Connection con = DBConnection.connection();
		ArrayList<HashtagSubscription> subscriptions = new ArrayList<HashtagSubscription>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`hashtagsubscription` WHERE `userId` = " + user.getId());
			
			while(rs.next()) {
				HashtagSubscription hs = new HashtagSubscription();
				hs.setRecipientId(rs.getInt("userId"));
				hs.setHashtagId(rs.getInt("hashtagId"));		
				subscriptions.add(hs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}

	public ArrayList<User> findAllFollowersOfHashtag(Hashtag h) {
		Connection con = DBConnection.connection();
		ArrayList<User> subscriptions = new ArrayList<User>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT `user`.`id`, `user`.`email`, `user`.`nickname`, `user`.`creationDate` "
											+ "FROM `dbmessenger`.`user` INNER JOIN `dbmessenger`.`hashtagsubscription` "
											+ "ON `dbmessenger`.`user`.`id` = `dbmessenger`.`hashtagsubscription`.`userId`"
											+ "WHERE `hashtagId` = " + h.getId());
			while(rs.next()) {
				User u = new User();
				u.setId(rs.getInt("id"));
				u.setEmail(rs.getString("email"));
				u.setNickname(rs.getString("nickname"));
				u.setCreationDate(rs.getDate("creationDate"));
				subscriptions.add(u);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}
}
