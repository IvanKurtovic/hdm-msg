package de.hdm.gruppe2.client.report;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.ClientsideSettings;
import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.bo.User;
import de.hdm.gruppe2.shared.report.AllFollowersOfUserReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

/**
 * Diese Klasse bildet die Oberfläche für den Report "Alle Follower eines Users"
 * ab. Es stellt eines ListBox zur Verfügung die alle vorhandenen User der Datenbank
 * präsentiert. Dazu liest diese Klasse bei Erzeugung via RPC-Call die Datenbank nach
 * allen Usern aus und speichert sie in eine ArrayList.
 * Der Anwender kann schließlich über diese Box einen User auswählen und den Report
 * über den Button "btnReport" generieren lassen. Dieser wird als reiner HTML Text
 * in den "footer_wrap" der HTML Seite geschrieben.
 * 
 * @author Kurtovic
 * @author Korkmaz
 *
 */
public class AllFollowersOfUserForm extends VerticalPanel {

	private ReportRPCAsync reportGenerator = null;
	private ArrayList<User> users = null;
	
	private ListBox lbUsers = new ListBox();
	private Button btnReport = new Button("Laden");
	
	@Override
	public void onLoad() {
		
		reportGenerator = ClientsideSettings.getReportGenerator();
		
		this.getAllUsers();
		
		Grid mainGrid = new Grid(3, 2);
		
		Label lblTitle = new Label("Alle Follower eines Nutzers");
		
		btnReport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(lbUsers.getSelectedIndex() == -1) {
					ClientsideSettings.getLogger().severe("Kein hashtag ausgewählt.");
					return;
				}
				
				loadReport(users.get(lbUsers.getSelectedIndex()));
			}
		});
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lbUsers);
		mainGrid.setWidget(2, 0, btnReport);
		
		this.add(mainGrid);
		
	}
	
	private void getAllUsers() {
		reportGenerator.findAllUsers(new AsyncCallback<ArrayList<User>>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						 "User konnten nicht geladen werden!");
			}

			@Override
			public void onSuccess(ArrayList<User> result) {
				users = result;
				
				lbUsers.clear();
				
				for(User u : users) {
					lbUsers.addItem(u.getNickname());
				}
			}
		});
	}
	
	private void loadReport(User u) {
		reportGenerator.createAllFollowersOfUserReport(u, new AsyncCallback<AllFollowersOfUserReport>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllFollowersOfUserReport result) {
				if (result != null) {
					HTMLReportWriter writer = new HTMLReportWriter();
					writer.process(result);
					RootPanel.get("footer_wrap").clear();
					RootPanel.get("footer_wrap").add(new HTML(writer.getReportText()));
				}
			}	
		});
	}
}
