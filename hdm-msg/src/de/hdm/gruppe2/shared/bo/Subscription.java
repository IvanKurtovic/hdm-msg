package de.hdm.gruppe2.shared.bo;

public class Subscription extends BusinessObject {

	private static final long serialVersionUID = 1L;	
	
	private int recipientId; 
	
	
	public int getRecipientId(){
		return recipientId;
		
	}
	
	public void setRecipientId(int recipientId){
		this.recipientId = recipientId;
	}
}
