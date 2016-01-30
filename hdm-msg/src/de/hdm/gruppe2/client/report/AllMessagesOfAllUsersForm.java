package de.hdm.gruppe2.client.report;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.hdm.gruppe2.client.ClientsideSettings;
import de.hdm.gruppe2.shared.ReportRPCAsync;
import de.hdm.gruppe2.shared.report.AllMessagesOfAllUsersReport;
import de.hdm.gruppe2.shared.report.HTMLReportWriter;

/**
 * Diese Klasse bildet die Oberfläche für den Report "Alle Nachrichten aller Nutzer"
 * ab. Bei Aufruf wird über einen RPC-Call des ReportRPCAsync Interfaces die Datenbank
 * ausgelesen und anschließend ein Report HTML Text generiert. Dieser wird in den 
 * "footer_wrap" der HTML Seite geschrieben und dem Nutzer präsentiert.
 * 
 * @author Kurtovic
 * @author Korkmaz
 *
 */
public class AllMessagesOfAllUsersForm extends VerticalPanel {

	private ReportRPCAsync reportGenerator = null;
	
	@Override
	public void onLoad() {
		
		reportGenerator = ClientsideSettings.getReportGenerator();
		
		reportGenerator.createAllMessagesOfAllUsersReport(new AsyncCallback<AllMessagesOfAllUsersReport>() {

			@Override
			public void onFailure(Throwable caught) {
				ClientsideSettings.getLogger().severe(
						"Erzeugen des Reports fehlgeschlagen!");
			}

			@Override
			public void onSuccess(AllMessagesOfAllUsersReport result) {
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
