package de.hdm.gruppe2.shared.bo;

/**
 * Nutzer-Klasse zur Verwaltung der Nutzerdaten
 * @author Ioannidou
 */
public class User extends BusinessObject {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Die Nutzer haben im System die Attribute
	 * Vorname, Nachname & Emailadresse 
	 * Für die Nutzerverwaltung und den Login 
	 * wird die Google Account API verwendet
	 * hierzu brauchen wir eine Google ID
	 */
	private String firstName;
	private String lastName;
	private String googleId;
	private String email;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getGoogleId() {
		return googleId;
	}
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setGoogleID(String string) {
		// TODO Auto-generated method stub
		
	}
	public String getGoogleID() {
		// TODO Auto-generated method stub
		return null;
	}
}