package de.hdm.gruppe2.shared.bo;

import java.io.Serializable;
import java.sql.Date;
/**
 * <p>
 * Die Klasse <code>BusinessObject</code> stellt die Basisklasse aller in diesem
 * Projekt für die Umsetzung des Fachkonzepts relevanten Klassen dar.
 * </p>
 * <p>
 * Zentrales Merkmal ist, dass jedes <code>BusinessObject</code> eine Nummer
 * besitzt, die man in einer relationalen Datenbank auch als Primärschlüssel
 * bezeichnen würde. Fernen ist jedes <code>BusinessObject</code> als
 * {@link Serializable} gekennzeichnet. Durch diese Eigenschaft kann jedes
 * <code>BusinessObject</code> automatisch in eine textuelle Form überführt und
 * z.B. zwischen Client und Server transportiert werden. Bei GWT RPC ist diese
 * textuelle Notation in JSON (siehe http://www.json.org/) kodiert.
 * </p>
 * 
 * @author Thies
 * @author Sari
 * @author Yilmaz
 */

public abstract class BusinessObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Eindeutige ID für alle Businessobjekte.
	 */
	private int id;
	
	/**
	 * Das Erstelldatum eines Objekts.
	 */
	private Date creationDate;

 /**
   * Auslesen des Erstelldatums.
   */
	public Date getCreationDate() {
		return creationDate;
	}

 /**
   * Setzen des Erstelldatums.
   */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

  /**
   * Auslesen der ID.
   */
	public int getId() {
		return id;
	}
	
  /**
   * Setzen der ID.
   */
	public void setId(int id) {
		this.id = id;
	}
}