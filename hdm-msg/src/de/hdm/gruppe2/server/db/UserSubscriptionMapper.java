package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import de.hdm.gruppe2.shared.bo.HashtagSubscription;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.bo.UserSubscription;

public class UserSubscriptionMapper {

	private static UserSubscriptionMapper userSubscriptionMapper = null;
	
	protected UserSubscriptionMapper() {}
	
	public static UserSubscriptionMapper userSubscriptionMapper() {
		if(userSubscriptionMapper == null) {
			userSubscriptionMapper = new UserSubscriptionMapper();
		}
		
		return userSubscriptionMapper;
	}
	
	public void insert(UserSubscription us) {
		Connection con = DBConnection.connection();
		
		try {
			if(findByRecipientAndSenderId(us.getRecipientId(), us.getSenderId()) == null) {
				Statement stmt = con.createStatement();			
				stmt.executeUpdate("INSERT INTO `dbmessenger`.`usersubscription`(`posterId`, `subscriberId`) VALUES (" + us.getSenderId() + ", " + us.getRecipientId() + " )");					
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public UserSubscription findByRecipientAndSenderId(int recipientId, int senderId) {
		Connection con = DBConnection.connection();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`usersubscription` WHERE `posterId` = " + senderId + " AND `subscriberId` = " + recipientId);
			
			if(rs.next()) {
				UserSubscription us = new UserSubscription();
				us.setSenderId(rs.getInt("posterId"));
				us.setRecipientId(rs.getInt("subscriberId"));		
				return us;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<UserSubscription> findAllUserSubscriptionsOfUser(User user) {
		Connection con = DBConnection.connection();
		ArrayList<UserSubscription> subscriptions = new ArrayList<UserSubscription>();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM `dbmessenger`.`usersubscription` WHERE `subscriberId` = " + user.getId());
			
			while(rs.next()) {
				UserSubscription us = new UserSubscription();
				us.setSenderId(rs.getInt("posterId"));
				us.setRecipientId(rs.getInt("subscriberId"));		
				subscriptions.add(us);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return subscriptions;
	}
	
	public void delete(int senderId, int recipientId) {
		Connection con = DBConnection.connection();
		
		try {			
			Statement stmt = con.createStatement();			
			stmt.executeUpdate("DELETE FROM `dbmessenger`.`usersubscription` WHERE `posterId` = " 
			+ senderId + " AND `subscriberId` = " 
			+ recipientId);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
