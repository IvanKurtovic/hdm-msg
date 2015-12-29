package de.hdm.gruppe2.shared.bo;

public class UserAbo extends Abo {

	private static final long serialVersionUID = 1L;
	
	User aboUser;

	public User getAboUser() {
		return aboUser;
	}

	public void setAboUser(User aboUser) {
		this.aboUser = aboUser;
	}

}