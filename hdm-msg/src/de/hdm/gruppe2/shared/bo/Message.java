package de.hdm.gruppe2.shared.bo;

import java.util.Vector;

/**
 * Nutzer-Klasse zur Verwaltung von Nachrichten
 * @author Ioannidou
 */
public class Message extends BusinessObject {

	private static final long serialVersionUID = 1L;

	private String text;
	private User sender;
	private Chat chat;
	private Vector<Hashtag> hashtagList;

	//Setzen und auslesen des Nachrichtentextes
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	// Setzen und auslesen des Senders einer Nachricht
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}

	
	//Setzen und auslesen des dazugehörigen Chats
	public Chat getChat() {
		return chat;
	}
	public void setChat(Chat chat) {
		this.chat = chat;
	}
	
	//Setzen und auslesen von Hashtags zu einer Nachricht
	public Vector<Hashtag> getHashtagList() {
		return hashtagList;
	}
	public void setHashtagList(Vector<Hashtag> hashtagList) {
		this.hashtagList = hashtagList;
	}
	
	
	
}
