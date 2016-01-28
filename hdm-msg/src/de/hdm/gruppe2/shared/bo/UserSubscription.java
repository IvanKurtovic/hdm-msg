package de.hdm.gruppe2.shared.bo;

public class UserSubscription extends Subscription {
	
	private static final long serialVersionUID = 1L;
	
	// Fremdschlüssel des Abonnierten
	private int senderId;	
	
	
	public int getSenderId(){
		return senderId;
	}
	
	public void setSenderId(int userId){
		this.senderId = userId;
	}
}
