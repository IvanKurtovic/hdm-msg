package de.hdm.gruppe2.server.report;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.gruppe2.server.MsgServiceImpl;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.ReportGenerator;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.Message;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.report.AllFollowersOfHashtagReport;
import de.hdm.gruppe2.shared.report.AllFollowersOfUserReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfAllUsersReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfUserReport;
import de.hdm.gruppe2.shared.report.Column;
import de.hdm.gruppe2.shared.report.CompositeParagraph;
import de.hdm.gruppe2.shared.report.Row;
import de.hdm.gruppe2.shared.report.SimpleParagraph;

/**
 * Implementierung des <code>ReportGenerator</code>-Interface. Die technische
 * Realisierung bzgl. <code>RemoteServiceServlet</code> bzw. GWT RPC erfolgt
 * analog zu {@MsgServiceImpl}. Für Details zu GWT RPC siehe dort.
 * 
 * @see ReportGenerator
 * @author thies
 * @author Korkmaz
 */
public class ReportGeneratorImpl extends RemoteServiceServlet implements ReportGenerator {
	  
	/**
	* Ein ReportGenerator benötigt Zugriff auf den MsgService, da dieser die
	* essentiellen Methoden für die Koexistenz von Datenobjekten (vgl.
	* bo-Package) bietet.
	*/
	private MsgService msgSvc = null;

	/**
	* <p>
	* Ein <code>RemoteServiceServlet</code> wird unter GWT mittels
	* <code>GWT.create(Klassenname.class)</code> Client-seitig erzeugt. Hierzu
	* ist ein solcher No-Argument-Konstruktor anzulegen. Ein Aufruf eines anderen
	* Konstruktors ist durch die Client-seitige Instantiierung durch
	* <code>GWT.create(Klassenname.class)</code> nach derzeitigem Stand nicht
	* möglich.
	* </p>
	* <p>
	* Es bietet sich also an, eine separate Instanzenmethode zu erstellen, die
	* Client-seitig direkt nach <code>GWT.create(Klassenname.class)</code>
	* aufgerufen wird, um eine Initialisierung der Instanz vorzunehmen.
	* </p>
	*/
	public ReportGeneratorImpl() throws IllegalArgumentException {}
	
	/**
	* Initialsierungsmethode. Siehe dazu Anmerkungen zum No-Argument-Konstruktor.
	* 
	* @see #ReportGeneratorImpl()
	*/
	@Override
	public void init() throws IllegalArgumentException {
		/*
		* Ein ReportGeneratorImpl-Objekt instantiiert für seinen Eigenbedarf eine
		* MsgServiceImpl-Instanz.
		*/
		MsgServiceImpl ms = new MsgServiceImpl();
		ms.init();
		this.msgSvc = ms;
	}
	
	/**
	 * Auslesen des zugehörigen MsgService (interner Gebrauch).
	 * 
	 * @return Das MsgService Objekt
	 */
	protected MsgService getMsgService() {
		return this.msgSvc;
	}

	@Override
	public AllMessagesOfUserReport createAllMessagesOfUserReport(User u) throws IllegalArgumentException {
		
		if(this.getMsgService() == null)
			return null;
		
		/*
	     * Zunächst legen wir uns einen leeren Report an.
	     */
		AllMessagesOfUserReport result = new AllMessagesOfUserReport();
		
		// Jeder Report hat einen Titel (Bezeichnung / Überschrift).
		result.setTitle("Alle Nachrichten des Nutzers");
		
		/*
	     * Datum der Erstellung hinzufügen. new Date() erzeugt autom. einen
	     * "Timestamp" des Zeitpunkts der Instantiierung des Date-Objekts.
	     */
	    result.setCreated(new Date());
	    
	    /*
	     * Ab hier erfolgt die Zusammenstellung der Kopfdaten (die Dinge, die oben
	     * auf dem Report stehen) des Reports. Die Kopfdaten sind mehrzeilig, daher
	     * die Verwendung von CompositeParagraph.
	     */
	    CompositeParagraph header = new CompositeParagraph();
	    
	    // Email und Nickname des Users aufnehmen
	    header.addSubParagraph(new SimpleParagraph(u.getEmail() + ", "
	        + u.getNickname()));
	    
	    // Anwendernummer aufnehmen
	    header.addSubParagraph(new SimpleParagraph("Anwender.-Nr.: " + u.getId()));
	    
	    // Hinzufügen der zusammengestellten Kopfdaten zu dem Report
	    result.setHeaderData(header);
	    
	    /*
	     * Ab hier erfolgt ein zeilenweises Hinzufügen von Message-Informationen.
	     */
	    
	    /*
	     * Zunächst legen wir eine Kopfzeile für die Message-Tabelle an.
	     */
	    Row headline = new Row();
	    
	    /*
	     * Wir wollen Zeilen mit 4 Spalten in der Tabelle erzeugen. In die ersten
	     * Spalte schreiben wir die jeweilige Message-ID und in die zweite den
	     * aktuellen Text. Die drei weiteren Spalten enthalten die ID des Empfänger-Chats
	     * und den Erstellzeitpunkt. In der Kopfzeile legen wir also entsprechende
	     * Überschriften ab.
	     */
	    headline.addColumn(new Column("Msg.-ID."));
	    headline.addColumn(new Column("Text"));
	    headline.addColumn(new Column("Empfänger ID"));
	    headline.addColumn(new Column("Erstellungszeitpunkt"));
	    
	    // Hinzufügen der Kopfzeile
	    result.addRow(headline);
	    
	    /*
	     * Nun werden sämtliche Messages des Nutzers ausgelesen und deren Details
	     * sukzessive in die Tabelle eingetragen.
	     */
	    ArrayList<Message> messages = this.msgSvc.findAllMessagesOfUser(u);
	    
	    for (Message m : messages) {
	    	// Eine leere Zeile anlegen.
	        Row messageRow = new Row();

	        // Erste Spalte: Anwender-ID hinzufügen
	        messageRow.addColumn(new Column(String.valueOf(m.getId())));
	        // Zweite Spalte: Text
	        messageRow.addColumn(new Column(m.getText()));
	        // Dritte Spalte: Empfänger Chat ID
	        messageRow.addColumn(new Column(String.valueOf(m.getChatId())));
	        // Vierte Spalte: Erstellungszeitpunkt
	        messageRow.addColumn(new Column(m.getCreationDate().toString()));

	        // und schließlich die Zeile dem Report hinzufügen.
	        result.addRow(messageRow);
	    }

	    /*
	     * Zum Schluss müssen wir noch den fertigen Report zurückgeben.
	     */
	    return result;
	}

	@Override
	public AllMessagesOfAllUsersReport createAllMessagesOfAllUsersReport() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllMessagesOfPeriodReport createAllMessagesOfPeriodReport(String start, String end)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllFollowersOfHashtagReport createAllFollowersOfHashtagReport(Hashtag h) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AllFollowersOfUserReport createAllFollowersOfUserReport(User u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}
}
