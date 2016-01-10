package de.hdm.gruppe2.shared.bo;

import java.util.ArrayList;

public class Message extends BusinessObject {	
	
	private static final long serialVersionUID = 1L;
	
	private String text; 
	
	private User sender; 
	
	private ArrayList<Hashtag> hashtagList;	
	
	private ArrayList<User> receiver;   
	
	public String getText() {
		return text;
	}

	public void setText (String text) {
		this.text = text; 
	}
	public User getUser() {
		return sender;
	}

	public void setUser (User sender) {
		this.sender = sender; 
	}

	public ArrayList<User> getReceiver() {
		return this.receiver;
	}
	
	public void setReciver (ArrayList<User> receiver) {
		this.receiver = receiver; 
	}
	
	public ArrayList<Hashtag> getHashtagList() {
		return hashtagList; 
	}
	public void setHashtagList (ArrayList<Hashtag> hashtagList) {
		this.hashtagList = hashtagList; 
	}
}
// Author Cem