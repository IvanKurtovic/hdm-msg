package de.hdm.gruppe2.shared.bo;

/**
 * Realisierung der Hashtag-Klasse. Sie enthält alle notwendigen Informationen
 * der Hashtags dieser Anwendung. Darunter fällt das Schlagwort.
 * 
 * @author thies
 * @author Sari
 * @author Yilmaz
 * @version 1.0
 */
public class Hashtag extends BusinessObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Das Schlagwort des Hashtag Objekts
	 */
	private String keyword;  
	
	/**
	 * Auslesen des Schlagworts
	 */
	public String getKeyword(){
		return keyword;
	}
	
	/**
	 * Setzen des Schlagworts
	 */
	public void setKeyword(String keyword){
		this.keyword=keyword;
	}
}

