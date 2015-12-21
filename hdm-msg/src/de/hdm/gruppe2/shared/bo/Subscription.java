package de.hdm.gruppe2.shared.bo;

public class Subscription extends BusinessObject {

	private static final long serialVersionUID = 1L;	
	
	private User recipient; 
	
	
	public User getUser(){
		return recipient;
		
	}
	
	public void setUser(User recipient){
		this.recipient=recipient;
	}
}
