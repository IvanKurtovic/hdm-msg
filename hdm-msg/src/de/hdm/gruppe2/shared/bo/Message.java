package de.hdm.gruppe2.shared.bo;

import java.util.ArrayList;

public class Message extends BusinessObject {
	
	private static final long serialVersionUID = 1L;
	
	private String text; 
	
	// Fremdschlüssel des Users - Verfasser
	private int userId;
	
	// Fremdschlüssel des Chats - Empfänger
	private int chatId;
	
	private ArrayList<Hashtag> hashtagList;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getChatId() {
		return chatId;
	}
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	public ArrayList<Hashtag> getHashtagList() {
		return hashtagList;
	}
	public void setHashtagList(ArrayList<Hashtag> hashtagList) {
		this.hashtagList = hashtagList;
	}
}