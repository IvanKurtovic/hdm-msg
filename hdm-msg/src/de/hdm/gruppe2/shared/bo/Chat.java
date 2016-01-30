package de.hdm.gruppe2.shared.bo;

import java.util.ArrayList;

/**
 * Realisierung der Chat-Klasse. Sie enthält alle notwendigen Informationen
 * der Chats dieser Anwendung. Darunter fallen eine Teilnehmer- sowie Nachrichtenliste
 * und der Name des Chats.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class Chat extends BusinessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Eine Liste aller Teilnehmer des Chats
	 */
	private ArrayList<User> memberList;
	
	/**
	 * Eine Liste aller Nachrichten des Chats
	 */
	private ArrayList<Message> messageList;

	/**
	 * Der Name des Chats
	 */
	private String name;

	/**
	 * Auslesen der Teilnehmerliste
	 */
	public ArrayList<User> getMemberList() {
		return memberList;
	}
	
	/**
	 * Setzen der Teilnehmerliste
	 */
	public void setMemberList(ArrayList<User> memberList) {
		this.memberList = memberList;
	}

	/**
	 * Auslesen der Nachrichtenliste
	 */
	public ArrayList<Message> getMessageList() {
		return messageList;
	}

	/**
	 * Setzen der Nachrichtenliste
	 */
	public void setMessageList(ArrayList<Message> messageList) {
		this.messageList = messageList;
	}

	/**
	 * Auslesen des Chatnamens
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setzen des Chatnamens
	 */
	public void setName(String name) {
		this.name = name;
	}
}
