package de.hdm.gruppe2.shared.bo;

/**
 * Realisierung der Anwender-Klasse. Sie enthält alle notwendigen Informationen
 * der Nutzer dieser Anwendung. Darunter fallen der verwendete Spitzname und die
 * Email Adresse.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class User extends BusinessObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Der Spitzname des Anwenders
	 */
	private String nickname;
	
	/**
	 * Die Email Adresse des Nutzers
	 */
	private String email;

	/**
	 * Auslesen des Spitznamens
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Setzen des Spitznamens
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	/**
	 * Auslesen der Email Adresse
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setzen der Email Adresse
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}