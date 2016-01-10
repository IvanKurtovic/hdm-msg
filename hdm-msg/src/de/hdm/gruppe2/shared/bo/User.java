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
	private String firstName;
	private String lastName;
	private String email;
	private Date creationDate;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}