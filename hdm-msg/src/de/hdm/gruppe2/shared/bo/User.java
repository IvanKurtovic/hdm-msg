package de.hdm.gruppe2.shared.bo;

import java.sql.Date;

/**
 * Nutzer-Klasse zur Verwaltung der Nutzerdaten
 * @author Ioannidou
 */
public class User extends BusinessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Die Nutzer haben im System die Attribute
	 * Vorname, Nachname & Emailadresse 
	 * Für die Nutzerverwaltung und den Login 
	 * wird die Google Account API verwendet
	 * hierzu brauchen wir eine Google ID
	 */
	private String nickname;
	private String email;
	
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public boolean equals() {
		// TODO equals Methode anpassen
		return false;
	}
}