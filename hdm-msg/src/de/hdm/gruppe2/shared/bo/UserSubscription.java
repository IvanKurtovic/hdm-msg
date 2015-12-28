package de.hdm.gruppe2.shared.bo;

public class UserSubscription extends Subscription {
	
	private static final long serialVersionUID = 1L;
	
	private User sender;	
	
	
	public User getAboUser(){
		return sender;
	}
	
	public void setAboUser(User AboUser){
		this.sender=AboUser;
	}
}
