package de.hdm.gruppe2.shared.bo;

import java.util.ArrayList;

/**
 * Realisierung der Message-Klasse. Sie enthält alle notwendigen Informationen
 * der Nachrichten dieser Anwendung. Darunter fallen der Text, die Chat ID,
 * die Verfasser ID und eine Liste enthaltener Hashtags.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class Message extends BusinessObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Der Text des Nachrichtenobjekts
	 */
	private String text; 
	
	/**
	 *  Fremdschlüssel des Users - Verfasser
	 */
	private int userId;
	
	/**
	 *  Fremdschlüssel des Chats - Empfänger
	 */
	private int chatId;
	
	/**
	 * Liste aller Hashtags die im Text des Message Objekt enthalten sind
	 */
	private ArrayList<Hashtag> hashtagList;

	/**
	 * Auslesen des Textes
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Setzen des Textes
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * Auslesen der Verfasser ID
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * Setzen der Verfasser ID
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	/**
	 * Auslesen der Empfänger ID
	 */
	public int getChatId() {
		return chatId;
	}
	
	/**
	 * Setzen der Empfänger ID
	 */
	public void setChatId(int chatId) {
		this.chatId = chatId;
	}
	
	/**
	 * Auslesen der enthaltenen Hashtags
	 */	
	public ArrayList<Hashtag> getHashtagList() {
		return hashtagList;
	}
	
	/**
	 * Setzen der enthaltenen Hashtags
	 */
	public void setHashtagList(ArrayList<Hashtag> hashtagList) {
		this.hashtagList = hashtagList;
	}
}