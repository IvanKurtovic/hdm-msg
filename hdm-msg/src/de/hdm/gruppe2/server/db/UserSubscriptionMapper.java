package de.hdm.gruppe2.server.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdm.gruppe2.shared.bo.HashtagSubscription;
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
	
}
