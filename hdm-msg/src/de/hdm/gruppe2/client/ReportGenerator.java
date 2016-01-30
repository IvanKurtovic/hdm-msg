package de.hdm.gruppe2.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;

import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.report.AllMessagesOfPeriodReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

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
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HTML("MESSAGES OF USER REPORT"));
			}
		};
		
		Command allMessagesOfAllUsers = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HTML("ALL MESSAGES OF ALL USERS REPORT"));
			}
		};
		
		Command allMessagesOfPeriod = new Command() {
			public void execute() {
				loadReport3();
			}
		};
		
		Command allFollowersOfUser = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HTML("FOLLOWERS OF USER REPORT"));
			}
		};
		
		Command allFollowersOfHashtag = new Command() {
			public void execute() {
				RootPanel.get("content_wrap").clear();
				RootPanel.get("content_wrap").add(new HTML("FOLLOWERS OF HASHTAG REPORT"));
			}
		};
		
		Command callMessenger = new Command() {
			public void execute() {
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
	
	private void loadReport3() {
		reportGenerator.createAllMessagesOfPeriodReport("2016-01-27 00:00:00", "2016-01-27 23:59:59", new AsyncCallback<AllMessagesOfPeriodReport> () {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("FAIL MAN");
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllMessagesOfPeriodReport result) {
				if(result != null) {
					Window.alert("SUCCESS");
					Window.alert(result.getTitle() + "\n" + result.getCreated() + "\n" + result.getHeaderData());
					Window.alert(result.getImprint().toString());
					Window.alert(result.getClass().toString());
					HTMLReportWriter writer = new HTMLReportWriter();
					writer.process(result);
					Window.alert(writer.getReportText());
					RootPanel.get("content_wrap").clear();
					RootPanel.get("content_wrap").add(new HTML(writer.getReportText()));
				}
			}	
		});
	}
}
