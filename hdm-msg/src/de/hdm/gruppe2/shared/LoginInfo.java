package de.hdm.gruppe2.shared;

import java.io.Serializable;

public class LoginInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Aktueller Loginzustand
	 */
	private boolean loggedIn = false;
	
	/**
	 * Die URL für die Login-Page
	 */
	private String loginUrl;
	
	/**
	 * Der Logout Link
	 */
	private String logoutUrl;
	
	/**
	 * Die Email Adresse des eingeloggten Nutzers
	 */
	private String emailAddress;
	
	/**
	 * Der Nickname des eingeloggten Nutzers
	 */
	private String nickname;
	
	/**
	 * Auslesen des Login Status
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	/**
	 * Setzen des Login Status
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Auslesen der Login URL
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	
	/**
	 * Setzen der Login URL
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	/**
	 * Auslesen der Logout URL
	 */
	public String getLogoutUrl() {
		return logoutUrl;
	}
	
	/**
	 * Setzen der Logout URL
	 */
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	/**
	 * Auslesen der Email Adresse
	 */
	public String getEmailAddress() {
		return emailAddress;
	}
	
	/**
	 * Setzen der Email Adresse
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	/**
	 * Auslesen des Nicknames des Nutzers
	 */
	public String getNickname() {
		return nickname;
	}
	
	/**
	 * Setzen des Nicknames des Nutzers
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
