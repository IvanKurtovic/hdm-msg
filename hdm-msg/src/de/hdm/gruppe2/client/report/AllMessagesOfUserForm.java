package de.hdm.gruppe2.client.report;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.ClientsideSettings;
import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.report.AllMessagesOfUserReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

/**
 * Diese Klasse bildet die Oberfläche für den Report "Alle Nachrichten eines Nutzers"
 * ab. Es stellt eine TextBox zur Eingabe der User Email bereit. Anhand dieser Email
 * wird anschließend über den Druck auf den Button "btnReport" der Report generiert und
 * als HTML Text in den "footer_wrap" der HTML Seite geschrieben.
 * 
 * @author Kurtovic
 * @author Korkmaz
 *
 */
public class AllMessagesOfUserForm extends VerticalPanel {

	private ReportRPCAsync reportGenerator = null;
	
	private TextBox tbUserEmail = new TextBox();
	private Button btnReport = new Button("Laden");
	
	@Override
	public void onLoad() {
		
		reportGenerator = ClientsideSettings.getReportGenerator();
		
		Grid mainGrid = new Grid(3, 2);
		
		Label lblTitle = new Label("Alle Nachrichten eines Nutzers");
		Label lblEmail = new Label("Email des Nutzers: ");
		
		btnReport.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(tbUserEmail.getText() == null || tbUserEmail.getText().isEmpty()) {
					ClientsideSettings.getLogger().severe("Keine Email eingetragen.");
					return;
				}
				
				loadReport(tbUserEmail.getText());
			}
		});
		
		mainGrid.setWidget(0, 0, lblTitle);
		mainGrid.setWidget(1, 0, lblEmail);
		mainGrid.setWidget(1, 1, tbUserEmail);
		mainGrid.setWidget(2, 0, btnReport);
				
		this.add(mainGrid);
	}
	
	private void loadReport(String userMail) {
		reportGenerator.createAllMessagesOfUserReport(userMail, new AsyncCallback<AllMessagesOfUserReport>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllMessagesOfUserReport result) {
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
