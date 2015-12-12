package de.hdm.gruppe2.shared.bo;

public class Message extends BusinessObject {
	
	
	private static long serialVersionUID = 1L;
	
	private String text; 
	
	private User user; 
	
	// HashtagListe fehlt!
	
	//private Sender sender;
	
	//private Reciever reciever;
	
	public String getText() {
		return text;
	}

	public void setText (String text) {
		this.text = text; 
	}
	public User getUser() {
		return user;
	}

	public void setUser (User user) {
		this.user = user; 
	}

	/*public String getReciever() {
		return reciever;
	}
	
	get und set Hashtag fehlt!
	
	public void setReciver (reciver Reciver) {
		this.reciver = reciver; 
	}*/

}
