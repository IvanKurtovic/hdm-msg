package de.hdm.gruppe2.shared.bo;

public abstract class Abo extends BusinessObject {

	private static final long serialVersionUID = 1L;
	
	User suscriber;

	public User getSuscriber() {
		return suscriber;
	}

	public void setSuscriber(User suscriber) {
		this.suscriber = suscriber;
	}

}
