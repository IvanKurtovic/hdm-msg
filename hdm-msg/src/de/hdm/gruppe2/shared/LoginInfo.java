package de.hdm.gruppe2.shared;

import java.io.Serializable;
import de.hdm.gruppe2.shared.bo.User;

/**
 * Loginklasse mit allen Daten über die 
 * aktuelle Nutzersession
 * 
 * @author Marina Ioannidou
 */
public class LoginInfo implements Serializable {
	private static final long serialVersionUID = -5207880593956618550L;
	
	private boolean loggedIn = false;
	private String loginUrl;
	private String logoutUrl;
	private String emailAddress;
	private User user;
	
	//Überprüfen ob der User eingeloggt ist
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	// User auf eingeloggt setzen
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	public String getLogoutUrl() {
		return logoutUrl;
	}
	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getGoogleID(){
		return user.getGoogleId();
	}
}