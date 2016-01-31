package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

import de.hdm.gruppe2.client.report.AllFollowersOfHashtagForm;
import de.hdm.gruppe2.client.report.AllFollowersOfUserForm;
import de.hdm.gruppe2.client.report.AllMessagesOfAllUsersForm;
import de.hdm.gruppe2.client.report.AllMessagesOfPeriodForm;
import de.hdm.gruppe2.client.report.AllMessagesOfUserForm;
import de.hdm.gruppe2.shared.ReportRPCAsync;

/**
 * Die EntryPoint Klasse des ReportGenerators. Er stellt den Startpunkt des Nutzers dar
 * und bietet ihm eine Menüleiste zur Navigation durch den ReportGenerator. Von hier aus
 * kann der User alle Reports generieren und anzeigen lassen oder zurück zum Messenger
 * wechseln.
 * 
 */
public class ReportGenerator implements EntryPoint {

	private ReportRPCAsync reportGenerator = ClientsideSettings.getReportGenerator();
	private MenuBar mainMenu = new MenuBar();
	
	@Override
	public void onModuleLoad() {
		
		if (reportGenerator == null) {
			reportGenerator = ClientsideSettings.getReportGenerator();
		}
		
		Command allMessagesOfUser = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new AllMessagesOfUserForm());
			}
		};
		
		Command allMessagesOfAllUsers = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new AllMessagesOfAllUsersForm());
			}
		};
		
		Command allMessagesOfPeriod = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new AllMessagesOfPeriodForm());
			}
		};
		
		Command allFollowersOfUser = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new AllFollowersOfUserForm());
			}
		};
		
		Command allFollowersOfHashtag = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new AllFollowersOfHashtagForm());
			}
		};
		
		Command callMessenger = new Command() {
			public void execute() {
				RootPanel.get("footer_wrap").clear();
				RootPanel.get("content_wrap").clear();
				Window.Location.assign(GWT.getHostPageBaseURL() + "HdmMsg.html");
			}
		};
		
		mainMenu.setAutoOpen(true);
		mainMenu.addItem("Report 1", allMessagesOfUser);
		mainMenu.addItem("Report 2", allMessagesOfAllUsers);
		mainMenu.addItem("Report 3", allMessagesOfPeriod);
		mainMenu.addItem("Report 4", allFollowersOfUser);
		mainMenu.addItem("Report 5", allFollowersOfHashtag);
		mainMenu.addItem("Messenger", callMessenger);
		
		RootPanel.get("header_wrap").add(mainMenu);
	}
}
