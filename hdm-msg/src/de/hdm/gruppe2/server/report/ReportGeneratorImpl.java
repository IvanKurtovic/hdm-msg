package de.hdm.gruppe2.server.report;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.hdm.gruppe2.server.MsgServiceImpl;
import de.hdm.gruppe2.shared.MsgService;
import de.hdm.gruppe2.shared.ReportGenerator;
import de.hdm.gruppe2.shared.bo.Hashtag;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.report.AllFollowersOfHashtagReport;
import de.hdm.gruppe2.shared.report.AllFollowersOfUserReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfAllUsersReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.AllMessagesOfUserReport;

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

	@Override
	public AllMessagesOfUserReport createAllMessagesOfUserReport(User u) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
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
